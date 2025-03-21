/*
 * Copyright 2020 Aleksei Balan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ab;

import com.flagstone.transform.FSBounds;
import com.flagstone.transform.FSButton;
import com.flagstone.transform.FSColor;
import com.flagstone.transform.FSColorTable;
import com.flagstone.transform.FSDefineButton;
import com.flagstone.transform.FSDefineButton2;
import com.flagstone.transform.FSDefineFont;
import com.flagstone.transform.FSDefineFont2;
import com.flagstone.transform.FSDefineMovieClip;
import com.flagstone.transform.FSDefineShape;
import com.flagstone.transform.FSDefineShape3;
import com.flagstone.transform.FSDefineText;
import com.flagstone.transform.FSDefineTextField;
import com.flagstone.transform.FSDoAction;
import com.flagstone.transform.FSFontInfo2;
import com.flagstone.transform.FSMovie;
import com.flagstone.transform.FSMovieObject;
import com.flagstone.transform.FSPlaceObject2;
import com.flagstone.transform.FSSetBackgroundColor;
import com.flagstone.transform.FSShowFrame;
import com.flagstone.translate.ASParser;
import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Clock2 {

  public static final int W = 320;
  public static final int H = 240;
  public static final double SCALE = H / 400.0; // 550x400 - original size
  public static final int DX = -40;

  public static Set<Integer> getRequiredIdentifiers(Object object, Map<Integer, FSMovieObject> movieObjects) {
    Set<Integer> ids = new LinkedHashSet<>();
    if (object instanceof FSDefineMovieClip) {
      FSDefineMovieClip movieClip = (FSDefineMovieClip) object;
      for (Object o : movieClip.getObjects()) {
        ids.addAll(getRequiredIdentifiers(o, movieObjects));
      }
    }
    if (object instanceof FSDefineButton2) {
      FSDefineButton2 button = (FSDefineButton2) object;
      for (Object buttonRecord : button.getButtonRecords()) {
        ids.add(((FSButton) buttonRecord).getIdentifier());
      }
    }
    if (object instanceof FSPlaceObject2) {
      FSPlaceObject2 placeObject = (FSPlaceObject2) object;
      int id = placeObject.getIdentifier();
      ids.addAll(getRequiredIdentifiers(movieObjects.get(id), movieObjects));
      ids.add(id);
    }
    return ids;
  }

  public static int getIdentifier(Object o) {
    if (o instanceof FSDefineShape) {
      return ((FSDefineShape) o).getIdentifier();
    }
    if (o instanceof FSDefineShape3) {
      return ((FSDefineShape3) o).getIdentifier();
    }
    if (o instanceof FSDefineMovieClip) {
      return ((FSDefineMovieClip) o).getIdentifier();
    }
    if (o instanceof FSFontInfo2) {
      return ((FSFontInfo2) o).getIdentifier();
    }
    if (o instanceof FSDefineFont) {
      return ((FSDefineFont) o).getIdentifier();
    }
    if (o instanceof FSDefineText) {
      return ((FSDefineText) o).getIdentifier();
    }
    if (o instanceof FSDefineButton) {
      return ((FSDefineButton) o).getIdentifier();
    }
    if (o instanceof FSDefineButton2) {
      return ((FSDefineButton2) o).getIdentifier();
    }
    return 0;
  }

  @SneakyThrows
  public static byte[] construct(int version, String actionScriptFile) {
    // load movie
    byte[] scBytes = new byte[710241];
    DataInputStream dataInputStream =
        new DataInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("speedycat.swf"));
    dataInputStream.readFully(scBytes);
    FSMovie sc = new FSMovie(scBytes);

    // load movie objects to the map
    List<FSPlaceObject2> placeObjects = new ArrayList<>();
    Map<Integer, FSMovieObject> movieObjects = new HashMap<>();
    for (int i = 0; i < sc.getObjects().size(); i++) {
      FSMovieObject o = (FSMovieObject) sc.getObjects().get(i);
      movieObjects.put(getIdentifier(o), o);
      if (o instanceof FSPlaceObject2) {
        placeObjects.add((FSPlaceObject2) o);
      }
      if (o instanceof FSShowFrame) {
        break; // limit to normal speed
      }
    }

    // replace button with shape
    FSPlaceObject2 moon = placeObjects.get(2);
    int oldId = moon.getIdentifier();
    FSDefineButton2 button = (FSDefineButton2) movieObjects.get(oldId);
    FSButton moonButtonUp = (FSButton) button.getButtonRecords().get(0);
    int newId = moonButtonUp.getIdentifier();
    moon.setIdentifier(newId);
    // replace button with sprite
    FSDefineMovieClip sprite = new FSDefineMovieClip(oldId);
    sprite.add(new FSPlaceObject2(newId,
        moonButtonUp.getLayer(), moonButtonUp.getTransform(), moonButtonUp.getColorTransform()));
    movieObjects.put(oldId, sprite);
    moon.setIdentifier(oldId);

    // make a list of required objects
    placeObjects.remove(0); // 0=music, clouds, moon, cat, clouds
    Set<Integer> requiredObjectIds = new LinkedHashSet<>();
    for (FSPlaceObject2 placeObject : placeObjects) {
      requiredObjectIds.addAll(getRequiredIdentifiers(placeObject, movieObjects));
    }
    List<Object> requiredObjects =
        requiredObjectIds.stream().filter(i -> i > 0).map(movieObjects::get).collect(Collectors.toList());

    // create a new movie
    FSMovie movie = new FSMovie("UTF8", "FWS", version, new FSBounds(0, 0, W * 20, H * 20),
        sc.getFrameRate() / 2, new ArrayList<>()); // 50% 12fps slowcat =^_^=
    movie.add(new FSSetBackgroundColor(new FSColor(0x00, 0x00, 0x66, 0xFF))); // navy blue

    FSDefineFont2 sans = new FSDefineFont2(movie.newIdentifier(), "_sans");
    movie.add(sans);

    FSDefineTextField textHrMin = new FSDefineTextField(movie.newIdentifier(), movie.getFrameSize());
    textHrMin.setFontIdentifier(sans.getIdentifier());
    textHrMin.setColor(FSColorTable.yellow());
    textHrMin.setReadOnly(true);
    textHrMin.setBounds(new FSBounds(0, 0, W * 20, H * 20));
    textHrMin.setFontHeight(H * 4);
    textHrMin.setAlignment(FSDefineTextField.AlignLeft);
    textHrMin.setVariableName("timeHoursMinutes");
    textHrMin.setInitialText("23:35");
    movie.add(textHrMin);
    int textHrMinX = W * 11 + DX; // 12
    int textHrMinY = H * 14 + DX; // 15
    movie.add(new FSPlaceObject2(textHrMin.getIdentifier(), 45, textHrMinX, textHrMinY));

    FSDefineTextField textShadow = new FSDefineTextField(textHrMin);
    textShadow.setIdentifier(movie.newIdentifier());
    textShadow.setColor(FSColorTable.black());
    movie.add(textShadow);
    movie.add(new FSPlaceObject2(textShadow.getIdentifier(), 40, W / 5 + textHrMinX, W / 5 + textHrMinY));

    movie.add(new ArrayList<>(requiredObjects));
    movie.add(new ArrayList<>(placeObjects));
    for (FSPlaceObject2 placeObject : placeObjects) {
      float[][] transformMatrix = placeObject.getTransform().getMatrix();
      double scale = placeObject.getLayer() == 18 ? SCALE * 0.6 : SCALE;
      transformMatrix[0][0] *= scale;
      transformMatrix[0][2] *= scale;
      transformMatrix[1][1] *= scale;
      transformMatrix[1][2] *= scale;
      transformMatrix[0][2] -= W / 2; // 2.5% move to adjust 3% difference between 550x400 and 320x240
    }

    movie.add(new FSShowFrame());
    movie.add(new FSShowFrame());
    movie.add(new FSDoAction(new ASParser().parse(new File("src/main/resources/" + actionScriptFile)).encode(5)));
    movie.add(new FSShowFrame());
    movie.add(new FSShowFrame());
    return movie.encode();
  }
}
