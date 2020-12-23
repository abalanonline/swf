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
import com.flagstone.transform.FSDoAction;
import com.flagstone.transform.FSMovie;
import com.flagstone.transform.FSPlaceObject2;
import com.flagstone.transform.FSSetBackgroundColor;
import com.flagstone.transform.FSShowFrame;
import com.flagstone.translate.ASParser;
import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;

public class Clock2 {

  @SneakyThrows
  public static byte[] construct() {
    FSMovie movie = new FSMovie("UTF8", "FWS", 4, new FSBounds(0, 0, 320 * 20, 240 * 20), 2f, new ArrayList<>());

    movie.add(new FSSetBackgroundColor(FSColorTable.black()));

    FSDefineFont2 sans = new FSDefineFont2(movie.newIdentifier(), "_sans");
    movie.add(sans);

    movie.add(new FSShowFrame());
    movie.add(new FSDoAction(new ASParser().parse(new File("src/main/resources/clock1.as")).encode(5)));
    movie.add(new FSShowFrame());
    byte[] scriptBack = new ASParser().parse(new File("src/main/resources/clock1back.as")).encode(5);
    scriptBack = new byte[]{ // compilation failed
        (byte)0x96, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x96, (byte)0x03, (byte)0x00,
        (byte)0x00, (byte)0x34, (byte)0x00, (byte)0x22, (byte)0x96, (byte)0x03, (byte)0x00, (byte)0x00,
        (byte)0x31, (byte)0x00, (byte)0x0b, (byte)0x9f, (byte)0x01, (byte)0x00, (byte)0x01, (byte)0x00};
    movie.add(new FSDoAction(scriptBack));
    movie.add(new FSShowFrame());

    return movie.encode();
  }
}
