package org.example;
import java.io.File;
import java.util.List;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;
import com.vaadin.mpr.LegacyWrapper;
import com.vaadin.mpr.core.HasLegacyComponents;
import com.vaadin.mpr.core.MprTheme;
import com.vaadin.server.*;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;



@Route("test")
@MprTheme("mytheme")
public class MyUI extends Div {
    private CustomerService service = CustomerService.getInstance();
    private Grid<Customer> grid = new Grid<>(Customer.class);
    private TextField filterText = new TextField();
    private CustomerForm form = new CustomerForm(this);
    public MyUI(){
        final VerticalLayout layout = new VerticalLayout();
        filterText.setPlaceholder("filter by name...");
        filterText.addValueChangeListener(e -> updateList());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
        clearFilterTextBtn.setDescription("Clear the current filter");
        clearFilterTextBtn.addClickListener(e -> filterText.clear());
        CssLayout filtering = new CssLayout();
        filtering.addComponents(filterText, clearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        Button addCustomerBtn = new Button("Add new customer");
        addCustomerBtn.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.setCustomer(new Customer());
        });
        HorizontalLayout toolbar = new HorizontalLayout(filtering, addCustomerBtn);
        grid.setColumns("firstName", "lastName", "email");
        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSizeFull();
        grid.setSizeFull();
        main.setExpandRatio(grid, 1);

        // theme image
        ThemeResource resource = new ThemeResource("berg.jpg");
        Image image = new Image("My Theme Image", resource);
        layout.addComponents(toolbar, main);
        layout.addComponents(image);

        FileResource fileResource = new FileResource(new File(
                "berg.jpg"));
        Image image2 = new Image("berg.jpg", fileResource);
        layout.addComponents(image2);

        // fetch list of Customers from service and assign it to Grid
        updateList();
//        add(new LegacyWrapper(new VaadinDesignerV8_TestComponent()));
        add(new LegacyWrapper(new VaadinDesignerV8_TestComponent()));
        form.setVisible(false);
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                form.setVisible(false);
            } else {
                form.setCustomer(event.getValue());
            }
        });


    }
    public void updateList() {
        List<Customer> customers = service.findAll(filterText.getValue());
        grid.setItems(customers);
    }

}