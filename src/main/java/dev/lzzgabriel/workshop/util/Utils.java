package dev.lzzgabriel.workshop.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

public class Utils {

  public static Stage currentStage(ActionEvent e) {
    return (Stage) ((Node) e.getSource()).getScene().getWindow();
  }

  public static Integer tryParseToInt(String str) {
    try {
      return Integer.parseInt(str);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  public static <T> void formatTableColumnDate(TableColumn<T, LocalDate> tableColumn, String format) {
    tableColumn.setCellFactory(column -> {
      TableCell<T, LocalDate> cell = new TableCell<T, LocalDate>() {
        private DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);

        @Override
        protected void updateItem(LocalDate item, boolean empty) {
          super.updateItem(item, empty);
          if (empty) {
            setText(null);
          } else {
            setText(dtf.format(item));
          }
        }
      };
      return cell;
    });
  }

  public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
    tableColumn.setCellFactory(column -> {
      TableCell<T, Double> cell = new TableCell<T, Double>() {
        @Override
        protected void updateItem(Double item, boolean empty) {
          super.updateItem(item, empty);
          if (empty) {
            setText(null);
          } else {
            Locale.setDefault(Locale.US);
            setText(String.format("%." + decimalPlaces + "f", item));
          }
        }
      };
      return cell;
    });
  }

}
