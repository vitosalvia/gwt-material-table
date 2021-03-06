/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2016 GwtMaterialDesign
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package gwt.material.design.client;

import gwt.material.design.client.base.TableBaseTest;
import gwt.material.design.client.model.Person;
import gwt.material.design.client.ui.table.MaterialDataTable;
import gwt.material.design.client.ui.table.MaterialInfiniteDataTable;
import org.junit.Test;

public class MaterialTableTestComponent extends TableBaseTest {

    @Test
    public void testStandardTable() {
        MaterialDataTable<Person> standardTable = new MaterialDataTable<>();
        checkTable(standardTable);
    }

    @Test
    public void testInfiniteTable() {
        MaterialInfiniteDataTable<Person> infiniteDataTable = new MaterialInfiniteDataTable<>();
        checkTable(infiniteDataTable);
    }
}
