package org.japura.examples.gui.checkcombobox.example1;

import org.japura.examples.gui.AbstractExample;
import org.japura.examples.gui.Colors;
import org.japura.gui.Anchor;
import org.japura.gui.BatchSelection;
import org.japura.gui.CheckComboBox;
import org.japura.gui.EmbeddedComponent;
import org.japura.gui.event.ListCheckListener;
import org.japura.gui.event.ListEvent;
import org.japura.gui.model.ListCheckModel;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.util.List;

public class Example1 extends AbstractExample {

  @Override
  protected Component buildExampleComponent() {
    List<String> colors = Colors.getColors();

    final CheckComboBox ccb = new CheckComboBox();
    ccb.setTextFor(CheckComboBox.NONE, "* any item selected *");
    ccb.setTextFor(CheckComboBox.MULTIPLE, "* multiple items *");
    ccb.setTextFor(CheckComboBox.ALL, "* all selected *");
    BatchSelection bs = new InfoComponent();
    EmbeddedComponent comp = new EmbeddedComponent(bs, Anchor.SOUTH);
    ccb.setEmbeddedComponent(comp);
    ccb.setToolTipText("Test text from denny");

    ListCheckModel model = ccb.getModel();
    for (String color : colors) {
      model.addElement(color);
    }
    model.addElement("c1");
    model.addElement("c2");
    model.addElement("c3");
    model.setCheck("c2");

    return ccb;
  }

  public static void main(String[] args) {
    Example1 example = new Example1();
    example.runExample();
  }

  public static class InfoComponent extends BatchSelection {

    private JLabel countLabel;
    private ListCheckListener modelListener;
    private ListDataListener listDataListener;

    private JLabel getCountLabel() {
      if (countLabel == null) {
        countLabel = new JLabel("0/0");
      }
      return countLabel;
    }

    public InfoComponent() {
      setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      setLayout(new BorderLayout());
      add(getCountLabel(), BorderLayout.EAST);
    }

    private void updateCount(ListCheckModel model) {
      int selected = model.getChecksCount();
      int size = model.getSize();
      getCountLabel().setText( selected + "/" + size);
    }

    @Override
    protected void unregisterModel() {
      if (modelListener != null) {
        getModel().removeListCheckListener(modelListener);
      }
      modelListener = null;

      if (listDataListener != null) {
        getModel().removeListDataListener(listDataListener);
      }
      listDataListener = null;
    }

    @Override
    protected void registerModel(final ListCheckModel model) {
      modelListener = new ListCheckListener() {
        @Override
        public void removeCheck(ListEvent event) {
          updateCount(model);
        }

        @Override
        public void addCheck(ListEvent event) {
          updateCount(model);
        }
      };
      listDataListener = new ListDataListener() {
        @Override
        public void intervalAdded(ListDataEvent e) {
          updateCount(model);
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
          updateCount(model);
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
          updateCount(model);
        }
      };
      model.addListDataListener(listDataListener);
      model.addListCheckListener(modelListener);
    }
  }
}
