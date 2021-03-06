/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.jboss.ballroom.client.widgets.forms;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Heiko Braun
 * @date 2/21/11
 */
public class TextBoxItem extends FormItem<String> {

    protected TextBox textBox;
    private InputElementWrapper wrapper;

    public TextBoxItem(String name, String title) {
        super(name, title);

        textBox = new TextBox();
        textBox.setName(name);
        textBox.setTitle(title);
        textBox.setTabIndex(0);

        textBox.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                setModified(true);
                setUndefined(event.getValue().equals(""));
            }
        });
        wrapper = new InputElementWrapper(textBox, this);
    }

    public TextBoxItem(String name, String title, boolean isRequired) {
        super(name, title);

        setRequired(isRequired);

        textBox = new TextBox();
        textBox.setName(name);
        textBox.setTitle(title);
        textBox.setTabIndex(0);
        textBox.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                setModified(true);
                setUndefined(event.getValue().equals(""));
            }
        });
        wrapper = new InputElementWrapper(textBox, this);
    }

    @Override
    public void setFiltered(boolean filtered) {
        super.setFiltered(filtered);
        super.toggleAccessConstraint(textBox, filtered);
        textBox.setEnabled(!filtered);
        wrapper.setConstraintsApply(filtered);
    }

    @Override
    public Element getInputElement() {
        return textBox.getElement();
    }

    @Override
    public Widget asWidget() {
        return wrapper;
    }

    @Override
    public String getValue() {
        return textBox.getValue();
    }

    @Override
    public void resetMetaData() {
        super.resetMetaData();
        textBox.setValue(null);
    }

    @Override
    public void setExpressionValue(String expr) {
        this.expressionValue = expr;
        if(expressionValue!=null)
        {
            toggleExpressionInput(textBox, true);
            textBox.setValue(expressionValue);
        }
    }

    @Override
    public String asExpressionValue() {
        return textBox.getValue();
    }

    @Override
    public void setValue(String value) {
        toggleExpressionInput(textBox, false);
        textBox.setValue(value.trim());
    }

    @Override
    public void setEnabled(boolean b) {
        textBox.setEnabled(b);
    }

    @Override
    public void setErroneous(boolean b) {
        super.setErroneous(b);
        wrapper.setErroneous(b);
    }

    @Override
    public String getErrMessage() {
        return super.getErrMessage()+": no whitespace, no special chars";
    }

    @Override
    public boolean validate(String value) {

        if(expressionValue!=null || isExpressionScheme(textBox.getValue()))
        {
            return true;
        }
        else if(isRequired() && value.equals(""))
        {
            return false;
        }
        else
        {
            String updated = value.replace(" ", ""); // contains white space?
            return updated.equals(value);
        }
    }

    @Override
    public void clearValue() {
        textBox.setText("");
    }

    @Override
    protected void toggleExpressionInput(Widget target, boolean flag) {
        wrapper.setExpression(flag);
    }
}
