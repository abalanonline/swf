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
import com.flagstone.transform.FSDefineFont2;
import com.flagstone.transform.FSDefineTextField;
import com.flagstone.transform.FSMovie;
import com.flagstone.transform.FSPlaceObject2;
import com.flagstone.transform.FSSetBackgroundColor;
import com.flagstone.transform.FSShowFrame;
import lombok.SneakyThrows;

import java.util.ArrayList;

public class Clock1 {
  @SneakyThrows
  public static byte[] construct() {
    FSMovie movie = new FSMovie("UTF8", "FWS", 4, new FSBounds(0, 0, 320 * 20, 240 * 20), 2f, new ArrayList<>());

    movie.add(new FSSetBackgroundColor(FSColorTable.black()));

    FSDefineFont2 font = new FSDefineFont2(movie.newIdentifier(), "_typewriter");
    movie.add(font);

    FSDefineTextField textField = new FSDefineTextField(movie.newIdentifier(), new FSBounds(0, 0, 320 * 20, 240 * 20));
    textField.setFontIdentifier(font.getIdentifier());
    textField.setColor(FSColorTable.gray());
    textField.setInitialText("Flash App");
    textField.setFontHeight(24 * 20);
    textField.setReadOnly(true);
    movie.add(textField);

    movie.add(new FSPlaceObject2(textField.getIdentifier(), 10, 0, 0));

    movie.add(new FSShowFrame());

    return movie.encode();
  }
}
