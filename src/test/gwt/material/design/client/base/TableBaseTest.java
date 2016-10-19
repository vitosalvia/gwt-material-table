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
package gwt.material.design.client.base;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.Range;
import gwt.material.design.client.MaterialDesign;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.HideOn;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.data.SelectionType;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.factory.CustomCategoryFactory;
import gwt.material.design.client.factory.PersonRowFactory;
import gwt.material.design.client.model.Person;
import gwt.material.design.client.renderer.CustomRenderer;
import gwt.material.design.client.resources.MaterialTableBundle;
import gwt.material.design.client.ui.MaterialBadge;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.table.MaterialDataTable;
import gwt.material.design.client.ui.table.TableEvents;
import gwt.material.design.client.ui.table.TableScaffolding;
import gwt.material.design.client.ui.table.cell.Column;
import gwt.material.design.client.ui.table.cell.TextColumn;
import gwt.material.design.client.ui.table.cell.WidgetColumn;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static gwt.material.design.jquery.client.api.JQuery.$;

public class TableBaseTest extends TestCase {

    public <T extends MaterialDataTable<Person>> void checkTable(T table) {
        setup(table);
        checkProperties(table);
        checkEventHandlers(table);
        checkColumnsRows(table);
        checkStructure(table);
    }

    public <T extends MaterialDataTable<Person>> void checkProperties(T table) {
        RootPanel.get().add(table);
        final String HEIGHT = "calc(100vh - 131px)";
        table.setHeight(HEIGHT);
        table.setUseStickyHeader(true);
        assertTrue(table.isUseStickyHeader());
        table.setUseStickyHeader(false);
        assertFalse(table.isUseStickyHeader());
        table.setUseCategories(true);
        assertTrue(table.isUseCategories());
        table.setUseCategories(false);
        assertFalse(table.isUseCategories());
        table.setUseRowExpansion(true);
        assertTrue(table.isUseRowExpansion());
        table.setUseRowExpansion(false);
        assertFalse(table.isUseRowExpansion());
        table.setSelectionType(SelectionType.SINGLE);
        assertEquals(table.getSelectionType(), SelectionType.SINGLE);
        table.setSelectionType(SelectionType.MULTIPLE);
        assertEquals(table.getSelectionType(), SelectionType.MULTIPLE);
        table.setSelectionType(SelectionType.NONE);
        assertEquals(table.getSelectionType(), SelectionType.NONE);
    }

    public <T extends MaterialDataTable<Person>> void setup(T table) {
        // Inject Resources
        MaterialDesign.injectJs(MaterialTableBundle.INSTANCE.jQueryExt());
        MaterialDesign.injectJs(MaterialTableBundle.INSTANCE.stickyth());
        MaterialDesign.injectJs(MaterialTableBundle.INSTANCE.tableSubHeaders());
        MaterialDesign.injectJs(MaterialTableBundle.INSTANCE.greedyScroll());

        table.setRowFactory(new PersonRowFactory());
        table.setCategoryFactory(new CustomCategoryFactory());
        table.setRenderer(new CustomRenderer<>());
        table.setVisibleRange(0, 1000);
        Range range = table.getVisibleRange();
        assertEquals(range.getStart(), 0);
        assertEquals(range.getLength(), 1000);
    }

    public <T extends MaterialDataTable<Person>> void checkStructure(T table) {
        final String TABLE_TITLE = "table title";
        table.setTitle(TABLE_TITLE);
        assertEquals(table.getTitle(), TABLE_TITLE);
        TableScaffolding scaffolding = table.getScaffolding();
        assertTrue(scaffolding.getInfoPanel().getElement().hasClassName(TableCssName.INFO_PANEL));
        assertTrue(scaffolding.getTable().getElement().hasClassName(TableCssName.TABLE));
        assertTrue(scaffolding.getTableBody().getElement().hasClassName(TableCssName.TABLE_BODY));
        assertTrue(scaffolding.getToolPanel().getElement().hasClassName(TableCssName.TOOL_PANEL));
        assertTrue(scaffolding.getTopPanel().getElement().hasClassName(TableCssName.TOP_PANEL));
    }

    public <T extends MaterialDataTable<Person>> void checkColumnsRows(T table) {
        WidgetColumn<Person, MaterialImage> col1 = new WidgetColumn<Person, MaterialImage>() {
            @Override
            public MaterialImage getValue(Person object) {
                MaterialImage profile = new MaterialImage();
                profile.setUrl(object.getPicture());
                profile.setWidth("40px");
                profile.setHeight("40px");
                profile.setPadding(4);
                profile.setMarginTop(8);
                profile.setBackgroundColor(Color.GREY_LIGHTEN_2);
                profile.setCircle(true);
                return profile;
            }
        };
        table.addColumn(col1);
        assertEquals(table.getColumns().get(0), col1);

        Column<Person, ?> col2 = new TextColumn<Person>() {
            @Override
            public Comparator<? super RowComponent<Person>> getSortComparator() {
                return (o1, o2) -> o1.getData().getFirstName().compareToIgnoreCase(o2.getData().getFirstName());
            }

            @Override
            public String getValue(Person object) {
                return object.getFirstName();
            }
        };
        table.addColumn(col2, "First Name");
        assertEquals(table.getColumns().get(1), col2);
        assertEquals(col2.getName(), "First Name");
        assertTrue(col2.isSortable());

        Column<Person, ?> col3 = new TextColumn<Person>() {
            @Override
            public Comparator<? super RowComponent<Person>> getSortComparator() {
                return (o1, o2) -> o1.getData().getLastName().compareToIgnoreCase(o2.getData().getLastName());
            }

            @Override
            public String getValue(Person object) {
                return object.getLastName();
            }
        };
        table.addColumn(col3, "Last Name");
        assertEquals(table.getColumns().get(2), col3);
        assertEquals(col3.getName(), "Last Name");
        assertTrue(col2.isSortable());

        Column<Person, ?> col4 = new TextColumn<Person>() {
            @Override
            public boolean isNumeric() {
                return true;
            }

            @Override
            public HideOn getHideOn() {
                return HideOn.HIDE_ON_MED_DOWN;
            }

            @Override
            public Comparator<? super RowComponent<Person>> getSortComparator() {
                return (o1, o2) -> o1.getData().getPhone().compareToIgnoreCase(o2.getData().getPhone());
            }

            @Override
            public String getValue(Person object) {
                return object.getPhone();
            }
        };
        table.addColumn(col4, "Phone");
        assertEquals(table.getColumns().get(3), col4);
        assertEquals(col4.getName(), "Phone");
        assertTrue(col4.isNumeric());

        for (int i = 0; i < 4; i++) {
            final int index = i;
            Column<Person, ?> col = new TextColumn<Person>() {
                @Override
                public Comparator<? super RowComponent<Person>> getSortComparator() {
                    return (o1, o2) -> o1.getData().getPhone().compareToIgnoreCase(o2.getData().getPhone());
                }

                @Override
                public String getValue(Person object) {
                    return object.getPhone() + " " + index;
                }
            };
            table.addColumn(col, "Column " + index);
            assertEquals(table.getColumns().get(3 + (i + 1)), col);
            assertEquals(col.getName(), "Column " + index);
            assertTrue(col.isSortable());
        }

        Column<Person, ?> lastCol = new WidgetColumn<Person, MaterialBadge>() {
            @Override
            public TextAlign getTextAlign() {
                return TextAlign.CENTER;
            }

            @Override
            public MaterialBadge getValue(Person object) {
                MaterialBadge badge = new MaterialBadge();
                badge.setText("badge " + object.getId());
                badge.setBackgroundColor(Color.BLUE);
                badge.setLayoutPosition(Style.Position.RELATIVE);
                return badge;
            }
        };
        table.addColumn(lastCol);
        assertEquals(table.getColumns().get(8), lastCol);

        assertEquals(table.getColumns().size(), 9);

        // Generate 20 categories
        int rowIndex = 0;
        List<Person> people = new ArrayList<>();
        for(int k = 1; k <= 10; k++){
            // Generate 100 rows
            for(int i = 1; i <= 15; i++, rowIndex++) {
                people.add(new Person(i, "http://joashpereira.com/templates/material_one_pager/img/avatar1.png", "Field " + rowIndex, "Field " + i, "No " + i,"Category " + k));
            }
        }
    }

    public <T extends MaterialDataTable<Person>> void checkEventHandlers(T table) {
        // Category Closed
        final boolean[] isCategoryClosedFired = {false};
        table.addCategoryClosedHandler((e, param1) -> {
            isCategoryClosedFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.CATEGORY_CLOSED, null);
        assertTrue(isCategoryClosedFired[0]);
        // Category Opened
        final boolean[] isCategoryOpenedFired = {false};
        table.addCategoryOpenedHandler((e, param1) -> {
            isCategoryOpenedFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.CATEGORY_OPENED, null);
        assertTrue(isCategoryOpenedFired[0]);
        // Row Context Menu
        final boolean[] isRowContextMenuFired = {false};
        table.addRowContextMenuHandler((e, param1, param2, param3) -> {
            isRowContextMenuFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_CONTEXTMENU, null);
        assertTrue(isRowContextMenuFired[0]);
        // Row Count Change
        final boolean[] isRowCountChangeFired = {false};
        table.addRowCountChangeHandler((e, param1, param2) -> {
            isRowCountChangeFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_COUNT_CHANGE, null);
        assertTrue(isRowCountChangeFired[0]);
        // Row Double Click
        final boolean[] isRowDoubleClickFired = {false};
        table.addRowDoubleClickHandler((e, param1, param2, param3) -> {
            isRowDoubleClickFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_DOUBLECLICK, null);
        assertTrue(isRowDoubleClickFired[0]);
        // Row Expand
        final boolean[] isRowExpandFired = {false};
        table.addRowExpandHandler((e, param1) -> {
            isRowExpandFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_EXPAND, null);
        assertTrue(isRowExpandFired[0]);
        // Row Expanded
        final boolean[] isRowExpandedFired = {false};
        table.addRowExpandedHandler((e, param1) -> {
            isRowExpandedFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_EXPANDED, null);
        assertTrue(isRowExpandedFired[0]);
        // Row Long Press
        final boolean[] isRowLongPressFired = {false};
        table.addRowLongPressHandler((e, param1, param2, param3) -> {
            isRowLongPressFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_LONGPRESS, null);
        assertTrue(isRowLongPressFired[0]);
        // Row Select
        final boolean[] isRowSelectFired = {false};
        table.addRowSelectHandler((e, param1, param2, param3) -> {
            isRowSelectFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_SELECT, null);
        assertTrue(isRowSelectFired[0]);
        // Row Short Press
        final boolean[] isRowShortPressFired = {false};
        table.addRowShortPressHandler((e, param1, param2, param3) -> {
            isRowShortPressFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_SHORTPRESS, null);
        assertTrue(isRowShortPressFired[0]);
        // Select All
        final boolean[] isSelectAllFired = {false};
        table.addSelectAllHandler((e, param1, param2, param3) -> {
            isSelectAllFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.SELECT_ALL, null);
        assertTrue(isSelectAllFired[0]);
        // Sort Column
        final boolean[] isSortColumnFired = {false};
        table.addSortColumnHandler((e, param1, param2) -> {
            isSortColumnFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.SORT_COLUMN, null);
        assertTrue(isSortColumnFired[0]);
        // Stretch
        final boolean[] isStretchFired = {false};
        table.addStretchHandler((e, param1) -> {
            isStretchFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.STRETCH, null);
        assertTrue(isStretchFired[0]);
    }
}
