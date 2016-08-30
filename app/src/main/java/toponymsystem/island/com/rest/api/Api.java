/**
 * Copyright (C) 2015. Keegan小钢（http://keeganlee.me）
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package toponymsystem.island.com.rest.api;

import java.util.Map;

/**
 * Api接口
 *
 * @author CuiXiaoYong
 * @version 1.0
 * @date 13/3/21
 */
public interface Api {

    /**
     * @param map
     * @param Url
     * @return成功时返回json字符串
     */
    String request(Map<String, String> map, String Url);

    /**
     * @param Url
     * @return
     */
    String request(String Url);
}
