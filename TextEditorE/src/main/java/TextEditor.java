import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class TextEditor extends JFrame {
    JTextArea textArea;
    String fileAbsoluteName;
    JFileChooser fileChooser = new JFileChooser();
    JCheckBox useRegexCheckBox;
    JTextField searchField;
    ArrayList<int[]> foundIndexes = new ArrayList<>();
    int currentSearchResultIndex = -1;
    String searchQuery;

    public TextEditor() {
        super("Text Editor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setLayout(new BorderLayout());

        init();
    }

    //initialize all components on the frame
    private void init() {
        //north panel creating
        JPanel northPanel = new JPanel();
        this.add(northPanel, BorderLayout.NORTH);
        JButton openButton = new JButton();
        openButton.setName("OpenButton");
        openButton.setToolTipText("Open");
        openButton.setIcon(new javax.swing.ImageIcon("icons\\open16.png"));
        northPanel.add(openButton);
        JButton saveButton = new JButton();
        saveButton.setName("SaveButton");
        saveButton.setToolTipText("Save");
        saveButton.setIcon(new javax.swing.ImageIcon("icons\\save16.png"));
        northPanel.add(saveButton);

        //search block
        searchField = new JTextField(30);
        searchField.setName("SearchField");
        northPanel.add(searchField);
        useRegexCheckBox = new JCheckBox("Use regex");
        useRegexCheckBox.setFont(new Font("Arial", Font.BOLD, 13));
        useRegexCheckBox.setName("UseRegExCheckbox");
        northPanel.add(useRegexCheckBox);
        JButton startSearchButton = new JButton();
        startSearchButton.setName("StartSearchButton");
        startSearchButton.setToolTipText("Search");
        startSearchButton.setIcon(new javax.swing.ImageIcon("icons\\search16.png"));
        northPanel.add(startSearchButton);
        JButton previousMatchButton = new JButton();
        previousMatchButton.setName("PreviousMatchButton");
        previousMatchButton.setToolTipText("Previous");
        previousMatchButton.setIcon(new javax.swing.ImageIcon("icons\\prev16.png"));
        northPanel.add(previousMatchButton);
        JButton nextMatchButton = new JButton();
        nextMatchButton.setName("NextMatchButton");
        nextMatchButton.setToolTipText("Next");
        nextMatchButton.setIcon(new javax.swing.ImageIcon("icons\\next16.png"));
        northPanel.add(nextMatchButton);

        //text area and south scroll pane creation
        textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setName("ScrollPane");
        this.add(scrollPane, BorderLayout.CENTER);
        setVisible(true);

        //create menu
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.setName("MenuFile");
        menuBar.add(fileMenu);

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setName("MenuOpen");
        fileMenu.add(openMenuItem);
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setName("MenuSave");
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setName("MenuExit");
        fileMenu.add(exitMenuItem);

        JMenu searchMenu = new JMenu("Search");
        searchMenu.setMnemonic(KeyEvent.VK_S);
        searchMenu.setName("MenuSearch");
        menuBar.add(searchMenu);

        JMenuItem startSearchMenuItem = new JMenuItem("Start search");
        startSearchMenuItem.setName("MenuStartSearch");
        searchMenu.add(startSearchMenuItem);
        JMenuItem previousMatchMenuItem = new JMenuItem("Previous match");
        previousMatchMenuItem.setName("MenuPreviousMatch");
        searchMenu.add(previousMatchMenuItem);
        JMenuItem nextMatchMenuItem = new JMenuItem("Next match");
        nextMatchMenuItem.setName("MenuNextMatch");
        searchMenu.add(nextMatchMenuItem);
        JMenuItem useRegexMenuItem = new JMenuItem("Use regular expressions");
        useRegexMenuItem.setName("MenuUseRegExp");
        searchMenu.add(useRegexMenuItem);

        //File chooser
        fileChooser.setName("FileChooser");
        this.add(fileChooser);
        fileChooser.setVisible(false);

        //actions
        openButton.addActionListener(actionEvent -> openFileDialog());
        saveButton.addActionListener(actionEvent -> saveFileDialog());
        openMenuItem.addActionListener(actionEvent -> openFileDialog());
        saveMenuItem.addActionListener(actionEvent -> saveFileDialog());
        exitMenuItem.addActionListener(actionEvent -> dispose());

        startSearchButton.addActionListener(actionEvent -> startSearch());
        previousMatchButton.addActionListener(actionEvent -> previousMatch());
        nextMatchButton.addActionListener(actionEvent -> nextMatch());

        startSearchMenuItem.addActionListener(actionEvent -> startSearch());
        previousMatchMenuItem.addActionListener(actionEvent -> previousMatch());
        nextMatchMenuItem.addActionListener(actionEvent -> nextMatch());
        useRegexMenuItem.addActionListener(actionEvent -> toggleRegexCheckbox());
    }

    //do search in text
    private void startSearch() {
        currentSearchResultIndex = -1;
        foundIndexes.clear();

        if(useRegexCheckBox.isSelected()) {
            searchQuery = searchField.getText();
        } else {
            searchQuery = Pattern.quote(searchField.getText());
        }

        try {
            Pattern pattern = Pattern.compile(searchQuery);
            Matcher matcher = pattern.matcher(textArea.getText());
            while (matcher.find()) {
                foundIndexes.add(new int[]{matcher.start(), matcher.end()});
            }
        } catch (PatternSyntaxException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        if(foundIndexes.size() > 0) {
            currentSearchResultIndex = 0;
            selectSearchResult(foundIndexes.get(0)[0], foundIndexes.get(0)[1]);
        }

    }

    //Focus to previous search result
    private void previousMatch() {
        if(currentSearchResultIndex != -1 && foundIndexes.size() > 0) {
            if (currentSearchResultIndex > 0) {
                currentSearchResultIndex--;
            } else {
                currentSearchResultIndex = foundIndexes.size() - 1;
            }
            selectSearchResult(foundIndexes.get(currentSearchResultIndex)[0], foundIndexes.get(currentSearchResultIndex)[1]);
        }

    }

    //Focus to next search result
    private void nextMatch() {
        if(currentSearchResultIndex != -1 && foundIndexes.size() > 0) {
            if (currentSearchResultIndex < foundIndexes.size() - 1) {
                currentSearchResultIndex++;
            } else {
                currentSearchResultIndex = 0;
            }
            selectSearchResult(foundIndexes.get(currentSearchResultIndex)[0], foundIndexes.get(currentSearchResultIndex)[1]);
        }
    }

    //select necessary part in the text
    private void selectSearchResult(int start, int end) {
        textArea.setCaretPosition(end);
        textArea.select(start, end);
        textArea.grabFocus();
    }

    //toggle the checkbox on clicking from the main menu
    private void toggleRegexCheckbox() {
        useRegexCheckBox.setSelected(!useRegexCheckBox.isSelected());
    }

    //dialog for choosing of file to open
    private void openFileDialog() {
        fileChooser.setVisible(true);
        int returnValue = fileChooser.showOpenDialog(null);
        fileChooser.setVisible(false);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            fileAbsoluteName = selectedFile.getAbsolutePath();
            textArea.setText(new FileUtilities().load(fileAbsoluteName, this));
        } else {
            textArea.setText("");
        }
    }

    //dialog for choosing of file to save
    private void saveFileDialog() {
        fileChooser.setVisible(true);
        int returnValue = fileChooser.showSaveDialog(null);
        fileChooser.setVisible(false);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            fileAbsoluteName = selectedFile.getAbsolutePath();
            new FileUtilities().save(fileAbsoluteName, textArea.getText(), this);
        }
    }
}
