import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import javax.swing.*;

public class JTextFieldAppender extends AppenderSkeleton {

    private JTextArea textArea;

    public JTextFieldAppender(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    protected void append(LoggingEvent event) {
        SwingUtilities.invokeLater(() -> {
            textArea.setText(textArea.getText() + event.getRenderedMessage() + "\n");
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
