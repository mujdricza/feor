// 
// Originally written by Eva Mujdricza-Maydt, unfortunately, the source files are lost
// Decompiled by Procyon v0.5.36
// 

package feor;

import javax.swing.JFrame;
import java.io.File;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.filechooser.FileFilter;
import feor.gui.InputFileFilter;
import feor.objects.FEORSearch;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import java.awt.GridLayout;
import javax.swing.JSplitPane;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.JScrollPane;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.Map;
import feor.objects.FEORItem;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class GUI extends JPanel implements ActionListener
{
    private final String newline;
    protected static final String textFieldString = "JTextField";
    protected static final String buttonString = "JButton";
    protected static final String fileChooserString = "JFileChooser";
    protected static final String textAreaString = "JTextArea";
    private JTextArea logTextArea;
    private JTextArea logTextArea2;
    private JFileChooser fileChooser;
    private JTextField inputTextField;
    private List<FEORItem> FEORList;
    private Map<String, FEORItem> FEORCode2ItemMap;
    
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
    
    public GUI() {
        this.newline = Utils.FORMAT.NEWLINE.getValue();
        this.FEORList = null;
        this.FEORCode2ItemMap = null;
        this.setLayout(new BorderLayout());
        (this.logTextArea = new JTextArea(20, 20)).setMargin(new Insets(5, 5, 5, 5));
        this.logTextArea.setEditable(true);
        this.logTextArea.setAutoscrolls(true);
        this.logTextArea.setLineWrap(true);
        final JScrollPane logScrollPane = new JScrollPane(this.logTextArea);
        (this.logTextArea2 = new JTextArea(10, 50)).setMargin(new Insets(5, 5, 5, 5));
        this.logTextArea2.setEditable(true);
        this.logTextArea2.setAutoscrolls(true);
        this.logTextArea2.setLineWrap(true);
        final JScrollPane logScrollPane2 = new JScrollPane(this.logTextArea2);
        final JButton inputFileSelectionButton = new JButton("Select input file");
        inputFileSelectionButton.setActionCommand("JFileChooser");
        inputFileSelectionButton.addActionListener(this);
        (this.inputTextField = new JTextField(10)).setActionCommand("JTextField");
        this.inputTextField.addActionListener(this);
        final JLabel textFieldLabel = new JLabel("Code lookup: ");
        textFieldLabel.setLabelFor(this.inputTextField);
        final JPanel textControlsPane = new JPanel();
        final GridBagLayout gridbag = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        textControlsPane.setLayout(gridbag);
        final JLabel[] labels = { textFieldLabel };
        final JTextField[] textFields = { this.inputTextField };
        this.addLabelTextRows(labels, textFields, gridbag, textControlsPane);
        c.gridwidth = 0;
        c.anchor = 17;
        c.weightx = 1.0;
        textControlsPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Input Fields"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        final JTextArea inputTextArea = new JTextArea("(Currently does not work)");
        inputTextArea.setFont(new Font("Serif", 2, 16));
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        final JScrollPane inputTextAreaScrollPane = new JScrollPane(inputTextArea);
        inputTextAreaScrollPane.setVerticalScrollBarPolicy(22);
        inputTextAreaScrollPane.setPreferredSize(new Dimension(250, 250));
        inputTextAreaScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Plain Text"), BorderFactory.createEmptyBorder(5, 5, 5, 5)), inputTextAreaScrollPane.getBorder()));
        final JSplitPane splitPane = new JSplitPane(0, logScrollPane, logScrollPane2);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.5);
        splitPane.setAutoscrolls(true);
        final JPanel rightPane = new JPanel(new GridLayout(1, 0));
        rightPane.add(splitPane);
        rightPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Output Fields"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        final JPanel leftPane = new JPanel(new BorderLayout());
        leftPane.add(inputFileSelectionButton, "North");
        leftPane.add(textControlsPane, "South");
        leftPane.add(inputTextAreaScrollPane, "Center");
        this.add(leftPane, "Before");
        this.add(rightPane, "After");
    }
    
    private void addLabelTextRows(final JLabel[] labels, final JTextField[] textFields, final GridBagLayout gridbag, final Container container) {
        final GridBagConstraints c = new GridBagConstraints();
        c.anchor = 13;
        for (int numLabels = labels.length, i = 0; i < numLabels; ++i) {
            c.gridwidth = -1;
            c.fill = 0;
            c.weightx = 0.0;
            container.add(labels[i], c);
            c.gridwidth = 0;
            c.fill = 2;
            c.weightx = 1.0;
            container.add(textFields[i], c);
        }
    }
    
    private void init() throws IOException {
        if (this.FEORList == null) {
            this.FEORList = Main.readFEORList();
            System.out.println("READ FEOR ITEM LIST: " + this.FEORList.size());
            this.FEORCode2ItemMap = new TreeMap<String, FEORItem>();
            for (final FEORItem item : this.FEORList) {
                this.FEORCode2ItemMap.put(item.getCode().getCodeString(), item);
            }
        }
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (this.FEORList == null) {
            try {
                this.init();
            }
            catch (IOException ioe2) {
                this.logTextArea.append("Not found FEOR08 html files!" + this.newline);
                System.exit(1);
            }
        }
        if ("JTextField".equals(e.getActionCommand())) {
            final JTextField source = (JTextField)e.getSource();
            final String results = Main.lookupCode(this.FEORCode2ItemMap, source.getText());
            this.logTextArea2.setText(results + this.newline);
        }
        else if ("JTextArea".equals(e.getActionCommand())) {
            final JTextArea source2 = (JTextArea)e.getSource();
            final String results = Main.searchString(this.FEORList, source2.getText(), null);
            this.logTextArea.setText(results + this.newline);
        }
        else if ("JFileChooser".equals(e.getActionCommand())) {
            if (this.fileChooser == null) {
                (this.fileChooser = new JFileChooser()).addChoosableFileFilter(new InputFileFilter());
                this.fileChooser.setAcceptAllFileFilterUsed(false);
            }
            final int returnVal = this.fileChooser.showDialog(this, "Select");
            if (returnVal == 0) {
                final File selectedFile = this.fileChooser.getSelectedFile();
                String results2 = "NOT found input file: " + selectedFile.getName();
                try {
                    results2 = Main.searchFile(this.FEORList, selectedFile.getCanonicalPath(), Utils.FORMAT.CHARSETUTF8.getValue(), null);
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                this.logTextArea.setText(results2 + this.newline);
                final DefaultCaret caret = (DefaultCaret)this.logTextArea.getCaret();
                caret.setUpdatePolicy(2);
                try {
                    this.logTextArea.setCaretPosition(this.logTextArea.getLineStartOffset(0));
                    this.logTextArea.setSelectionStart(0);
                }
                catch (BadLocationException ble) {
                    ble.printStackTrace();
                }
            }
            else {
                this.logTextArea.append("Attachment cancelled by user." + this.newline);
            }
            this.logTextArea.setCaretPosition(this.logTextArea.getDocument().getLength());
            this.fileChooser.setSelectedFile(null);
        }
    }
    
    private static void createAndShowGUI() {
        final JFrame frame = new JFrame("FEOR08Searcher Demo v20171111");
        frame.setDefaultCloseOperation(3);
        frame.add(new GUI());
        frame.pack();
        frame.setVisible(true);
    }
}
