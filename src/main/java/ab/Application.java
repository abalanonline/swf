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
import com.flagstone.transform.FSColorTable;
import com.flagstone.transform.FSDefineMovieClip;
import com.flagstone.transform.FSDefineShape2;
import com.flagstone.transform.FSMovie;
import com.flagstone.transform.FSPlaceObject2;
import com.flagstone.transform.FSSetBackgroundColor;
import com.flagstone.transform.FSShowFrame;
import com.flagstone.transform.FSSolidFill;
import com.flagstone.transform.FSSolidLine;
import com.flagstone.transform.util.FSShapeConstructor;
import com.flagstone.translate.ASNode;
import com.flagstone.translate.ASParser;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings({"AssertWithSideEffects", "ConstantConditions"})
public class Application {

  static { // https://docs.oracle.com/javase/7/docs/technotes/guides/language/assert.html
    boolean assertsEnabled = false;
    assert assertsEnabled = true; // Intentional side effect!!!
    if (!assertsEnabled)
      throw new RuntimeException("Asserts must be enabled!!!");
  }

  @SneakyThrows
  public static void main(String[] args) {

    Files.write(Paths.get("target/clock1.swf"), Clock1.construct());

    int width = 4000;
    int height = 4000;

    FSMovie movie = new FSMovie();
    //FSMovie movie = new FSMovie("UTF8", "FWS", 4, new FSBounds(0, 0, 320, 240), 15f, new ArrayList<>());
    ASParser parser = new ASParser();
    FSShapeConstructor canvas = new FSShapeConstructor();

    // Compile the script containing the event handlers
    ASNode node = parser.parse(new File("src/main/resources/script.as"));
    byte[] bytes = node.encode(5);

    String byteCode = IntStream.range(0, bytes.length).map(i -> bytes[i] & 0xFF)
        .mapToObj(b -> String.format("%02x ", b)).collect(Collectors.joining());
    StringBuilder sbByteCode = new StringBuilder("#hexdata\n\n");
    for (int i = 0; i < bytes.length; i++) {
      sbByteCode.append(String.format("%02x ", bytes[i])).append(i % 8 == 7 ? "\n" : "");
    }
    byteCode = sbByteCode.append("\n").toString();
    Files.write(Paths.get("target/script.bytecode"), byteCode.getBytes());

    int shapeId = movie.newIdentifier();
    int clipId = movie.newIdentifier();

    // Create a simple shape to animate in a movie clip
    canvas.add(new FSSolidLine(20, FSColorTable.black()));
    canvas.add(new FSSolidFill(FSColorTable.red()));
    canvas.rect(2000, 2000);
    FSDefineShape2 rectangle = canvas.defineShape(shapeId);

    FSDefineMovieClip clip = new FSDefineMovieClip(clipId);
    clip.add(new FSPlaceObject2(shapeId, 1, 0, 0));
    clip.add(new FSShowFrame());

    // Set the handlers for the movie clip when it is displayed
    FSPlaceObject2 place = new FSPlaceObject2(clipId, 1, 0, 0);
    place.setEncodedEvents(bytes);

    // Put everything together in a movie
    movie.setFrameRate(12.0f);
    movie.setFrameSize(new FSBounds(-width, -height, width, height));
    movie.add(new FSSetBackgroundColor(FSColorTable.lightblue()));
    movie.add(rectangle);
    movie.add(clip);
    movie.add(place);
    movie.add(new FSShowFrame());

    movie.encodeToFile("target/example.swf");
  }

}
