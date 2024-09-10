import com.fasterxml.jackson.databind.ObjectMapper;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class SwingExample extends JDialog {
    private final Logger logger = Logger.getLogger(SwingExample.class);
    private JPanel contentPane;
    private JButton buttonSearch;
    private JButton buttonCancel;
    private JTable table1;
    private JTextField textForSearch;
    private JTabbedPane tabbedPane1;
    private JTextArea textLog;
    ObjectMapper objectMapper = new ObjectMapper();
    ArrayList<Furniture> furnitureArrayList = new ArrayList<>();
    Map<String, Furniture> furnitureMap = new HashMap<>();
    Furniture furniture;

    public SwingExample() {

        BasicConfigurator.configure();
        JTextFieldAppender textFieldAppender = new JTextFieldAppender(textLog);
        logger.addAppender(textFieldAppender);

        setContentPane(contentPane);
        setModal(true);
        logger.info("Initializing");
        for (int i = 1, j = 300300, k = 3700; i < 13; i++, j += 100, k += 50) {
            furniture = new Furniture("" + j, "Description" + i, "" + k);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://echo.free.beeceptor.com/request?article=" + furniture.getArticle() +
                            "&description=" + furniture.getDescription() + "&price=" + furniture.getPrice()))
                    .header("Accept", "application/json")
                    .GET() // GET is default
                    .build();
            logger.info("Request " + request.toString());
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                logger.info("Response "  + response.toString()); // Логирование ответа

                Map<String, Map<String, String>> map = objectMapper.readValue(response.body(), HashMap.class);
                Map<String, String> forJson = map.get("parsedQueryParams");
                String jsonFurniture = objectMapper.writeValueAsString(forJson);
                Furniture furnitureFromJson = objectMapper.readValue(jsonFurniture, Furniture.class);
                furnitureArrayList.add(furnitureFromJson);
                furnitureMap.put(furnitureFromJson.getArticle(), furnitureFromJson);
                logger.info("Added furniture: " + furnitureFromJson.getArticle());

            } catch (IOException | InterruptedException ex) {
                logger.error("Error processing request: " + ex.getMessage()); // Логирование ошибки
                throw new RuntimeException(ex);
            }
        }
        TableModel model = new FurnitureTableModel(furnitureArrayList);
        table1.setModel(model);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        table1.setRowSorter(sorter);

        buttonSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = textForSearch.getText();
                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
                logger.info("Search executed for: " + text);
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        logger.info("Application closed by user."); // Логирование закрытия приложения
        dispose();
    }

    public static void main(String[] args) {
        SwingExample dialog = new SwingExample();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
