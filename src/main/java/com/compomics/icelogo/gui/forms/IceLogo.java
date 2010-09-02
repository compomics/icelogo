package com.compomics.icelogo.gui.forms;

import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.enumeration.IceLogoEnum;
import com.compomics.icelogo.gui.component.MessagePanel;
import com.compomics.icelogo.gui.component.Messenger;
import com.compomics.icelogo.gui.forms.wizard.SamplingWizard;
import com.compomics.icelogo.gui.interfaces.Graphable;
import com.compomics.icelogo.gui.interfaces.GraphableAcceptor;
import com.compomics.icelogo.gui.interfaces.MessageAcceptor;
import com.jgoodies.looks.FontPolicies;
import com.jgoodies.looks.FontPolicy;
import com.jgoodies.looks.FontSet;
import com.jgoodies.looks.FontSets;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.SkyBluer;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.net.URL;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 27-okt-2008 Time: 17:01:28 The 'Logo ' class was created for
 */
public class IceLogo extends JFrame implements GraphableAcceptor {

    //gui elements
    private JPanel jpanContent;
    private JPanel jpanMain;
    private JPanel jpanStatus;
    private JPanel jpanMessage;
    private JToolBar tlbIcons;
    private JButton btnSampling;
    private JButton iSaveToPdfButton;
    private JButton btnClose;
    private JSpinner spinPValue;
    private JTabbedPane tabPanel;
    private JButton btnStatic;
    private JButton btnParam;
    private JButton btnColor;
    private JButton btnAaParam;
    private JRadioButton iceLogoRadioButton;
    private JRadioButton heatmapRadioButton;
    private JRadioButton barchartRadioButton;
    private JRadioButton sublogoRadioButton;
    private JRadioButton sequenceLogoRadioButton;
    private JRadioButton AAParameterMatrixRadioButton;
    private JButton addSpecies;
    private JProgressBar mainProgress;
    private JRadioButton conservationLogoRadioButton;
    private JMenuBar menubar;

    /**
     * String with the version of the program
     */
    public static String ICELOGO_VERSION;

    /**
     * Vector with savable panels
     */
    private Vector<Graphable> iGraphableElements = new Vector<Graphable>();
    /**
     * The main information feeder
     */
    private MainInformationFeeder iInfoFeeder = MainInformationFeeder.getInstance();


    public IceLogo(final String title) throws HeadlessException {
        super(title);

        //get the version and set the title
        ICELOGO_VERSION = getLastVersion();
        super.setTitle("Icelogo - " + ICELOGO_VERSION);

        // Look n feel.
        try {
            FontSet fontSet = FontSets.createDefaultFontSet(
                    new Font("Tahoma", Font.PLAIN, 11),    // control font
                    new Font("Tahoma", Font.PLAIN, 11),    // menu font
                    new Font("Tahoma", Font.BOLD, 11)     // title font
            );
            FontPolicy fixedPolicy = FontPolicies.createFixedPolicy(fontSet);
            PlasticLookAndFeel.setFontPolicy(fixedPolicy);
            PlasticLookAndFeel.setPlasticTheme(new SkyBluer());
            UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        this.setIconImage(new ImageIcon(getClass().getResource("/icons/comp.png")).getImage());
        //create the UI
        $$$setupUI$$$();
        //set the component listeners
        setListeners();
        //set the content pane
        setContentPane(jpanContent);
        //pack and set close configuration
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        //give welcom message
        Messenger.getInstance().sendMessage("Welcome to IceLogo - " + ICELOGO_VERSION + ".");
        //set visible
        setVisible(true);
        //set some things on the main information feeder
        iInfoFeeder.setUseIceLogo(iceLogoRadioButton.isSelected());
        iInfoFeeder.setUseBarchart(barchartRadioButton.isSelected());
        iInfoFeeder.setUseHeatmap(heatmapRadioButton.isSelected());
        iInfoFeeder.setUseSequenceLogo(sequenceLogoRadioButton.isSelected());
        iInfoFeeder.setUseSubLogo(sublogoRadioButton.isSelected());
        iInfoFeeder.setUseAaParameterGraph(AAParameterMatrixRadioButton.isSelected());

    }

    /**
     * This method packs the frame
     */
    public void pack() {
        super.pack();
        setSize(new Dimension(1000, 640));

    }

    /**
     * This method creates UI components
     */
    private void createUIComponents() {
        //create icon toolbar
        tlbIcons = new JToolBar(JToolBar.VERTICAL);

        // set the progressbar.
        mainProgress = Messenger.getInstance().getProgressBar();

        //close button
        btnClose = new JButton(new ImageIcon(ClassLoader.getSystemResource("icons/Exit.png")));

        //create the message panel
        jpanMessage = new MessagePanel();
        Messenger.getInstance().addMessageListener((MessageAcceptor) jpanMessage);
        //createMenu();

        //create the canvas for the svg figure
        JComponent lIceLogoCanvas = getIceLogoComponent();
        lIceLogoCanvas.setSize(new Dimension(500, 400));
        jpanMain = new JPanel();
        jpanMain.setBackground(Color.WHITE);
        jpanMain.add(lIceLogoCanvas, BorderLayout.CENTER);

        //create the P value spinner
        spinPValue = new JSpinner();
        double lPValue = MainInformationFeeder.getInstance().getPvalue();
        spinPValue.setModel(new SpinnerNumberModel(lPValue, 0.000000, 1.000000, 0.01));
        spinPValue.addChangeListener(new ChangeListener() {
            public void stateChanged(final ChangeEvent e) {
                MainInformationFeeder.getInstance().setPvalue((Double) ((JSpinner) e.getSource()).getValue());
            }
        });

        //create the buttons
        btnSampling = new JButton(new ImageIcon(ClassLoader.getSystemResource("icons/sampling.png")));
        iSaveToPdfButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("icons/Save.png")));
        btnStatic = new JButton(new ImageIcon(ClassLoader.getSystemResource("icons/static.png")));
        btnParam = new JButton(new ImageIcon(ClassLoader.getSystemResource("icons/param.png")));
        btnColor = new JButton(new ImageIcon(ClassLoader.getSystemResource("icons/color.png")));
        btnAaParam = new JButton(new ImageIcon(ClassLoader.getSystemResource("icons/aaparam.png")));
        addSpecies = new JButton(new ImageIcon(ClassLoader.getSystemResource("icons/adder.png")));

        //create the tabbed pane
        tabPanel = new JTabbedPane();
        tabPanel.setLayout(new BorderLayout());
    }

    public void writeChartAsPDF(OutputStream out) throws IOException {
        JFreeChart chart;
        int width = this.getSize().width;
        int height = this.getSize().height;
        FontMapper mapper = new DefaultFontMapper();

        Rectangle pagesize = new Rectangle(width, this.getSize().height);
        Document document = new Document(pagesize, 50f, 50f, 50f, 50f);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.addAuthor("LogoExpert");
            document.addSubject("Export");
            document.open();

            // First addComponent the JFreechart.
            Component c = tabPanel.getComponentAt(0);

            PdfContentByte cb = writer.getDirectContent();
            PdfTemplate tp = cb.createTemplate(c.getSize().width, c.getSize().width);
            Graphics2D g2 = tp.createGraphics(c.getSize().width, c.getSize().width, mapper);
            Rectangle2D r2D = new Rectangle2D.Double(0, 0, c.getSize().width, c.getSize().width);
            c.print(g2);
            cb.addTemplate(tp, 0, 0);
            g2.dispose();
            document.newPage();

            c = (tabPanel.getComponentAt(1));

            tp = cb.createTemplate(c.getSize().width, c.getSize().width);
            g2 = tp.createGraphics(c.getSize().width, c.getSize().width, mapper);
            r2D = new Rectangle2D.Double(0, 0, c.getSize().width, c.getSize().width);
            c.print(g2);
            cb.addTemplate(tp, 0, 0);
            g2.dispose();

        }
        catch (DocumentException de) {
            System.err.println(de.getMessage());
        }
        document.close();
    }

    public void addComponent(final JComponent comp, final String aTitle) {
        tabPanel.add(aTitle, comp);
        tabPanel.setSelectedComponent(comp);
    }

    /**
     * This method adds a savable pane
     *
     * @param aGraphable A panel that can be saved
     */
    public void addGraphable(Graphable aGraphable) {
        iGraphableElements.add(aGraphable);
    }

    /**
     * This method set the listeners
     */
    private void setListeners() {

        btnSampling.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                iInfoFeeder.setIceLogoType(IceLogoEnum.SAMPLING);
                iInfoFeeder.setStartPosition(1);
                removeAll();
                removeAllSavables();
                tabPanel.add("Home", SamplingWizard.getInstance(IceLogo.this));
                repaint();
            }
        });
        btnStatic.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                iInfoFeeder.setIceLogoType(IceLogoEnum.STATIC);
                removeAll();
                removeAllSavables();
                tabPanel.add("Home", StaticLogoForm.getInstance(IceLogo.this));
                tabPanel.setDoubleBuffered(true);
                repaint();
            }
        });
        btnParam.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                JFrame paramFrame = new JFrame("General iceLogo parameters");
                paramFrame.setIconImage(new ImageIcon(ClassLoader.getSystemResource("icons/param.png")).getImage());
                //create JFrame parameters
                IceLogoOptionForm lParam = new IceLogoOptionForm();
                paramFrame.setContentPane(lParam.getContentPane());
                paramFrame.setSize(650, 600);
                paramFrame.setLocation(100, 100);
                paramFrame.setVisible(true);
            }
        });
        addSpecies.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddSpeciesForm aAdd = new AddSpeciesForm();
            }
        });

        btnColor.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                JFrame colorFrame = new JFrame("Logo color chooser");
                colorFrame.setIconImage(new ImageIcon(ClassLoader.getSystemResource("icons/color.png")).getImage());
                //create JFrame parameters
                ColorChooserForm lColor = new ColorChooserForm();
                colorFrame.setContentPane(lColor.getContentPane());
                colorFrame.setSize(400, 400);
                colorFrame.setLocation(100, 100);
                colorFrame.setVisible(true);
            }
        });
        btnAaParam.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                JFrame paramFrame = new JFrame("Aa paramter chooser");
                paramFrame.setIconImage(new ImageIcon(ClassLoader.getSystemResource("icons/aaparam.png")).getImage());

                //create JFrame parameters
                AAIndexChooserForm lParam = new AAIndexChooserForm();
                paramFrame.setContentPane(lParam.getContentPane());
                paramFrame.setSize(600, 800);
                paramFrame.setLocation(100, 100);
                paramFrame.setVisible(true);
            }
        });


        iSaveToPdfButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                JFrame saveFrame = new JFrame("Save");
                saveFrame.setIconImage(new ImageIcon(ClassLoader.getSystemResource("icons/Save.png")).getImage());
                //create JFrame parameters
                GraphableSaverForm lSave = new GraphableSaverForm(iGraphableElements);
                saveFrame.setContentPane(lSave.getContentPane());
                saveFrame.setSize(600, 300);
                saveFrame.setLocation(100, 100);
                saveFrame.setVisible(true);
            }
        });


        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                close();
            }
        });

        iceLogoRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iInfoFeeder.setUseIceLogo(iceLogoRadioButton.isSelected());
            }
        });

        barchartRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iInfoFeeder.setUseBarchart(barchartRadioButton.isSelected());
            }
        });

        heatmapRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iInfoFeeder.setUseHeatmap(heatmapRadioButton.isSelected());
            }
        });

        sequenceLogoRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iInfoFeeder.setUseSequenceLogo(sequenceLogoRadioButton.isSelected());
            }
        });

        sublogoRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iInfoFeeder.setUseSubLogo(sublogoRadioButton.isSelected());
            }
        });

        AAParameterMatrixRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iInfoFeeder.setUseAaParameterGraph(AAParameterMatrixRadioButton.isSelected());
            }
        });

        conservationLogoRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iInfoFeeder.setUseConservationLogo(conservationLogoRadioButton.isSelected());
            }
        });
    }

    public void removeAll() {
        // Reset all but one tab.
        Component[] lComponents = tabPanel.getComponents();
        for (int i = 0; i < lComponents.length; i++) {

            Component lComponent = lComponents[i];

            tabPanel.remove(lComponent);
        }
        // All have been deleted, reset the first component.
    }

    public void removeAllSavables() {
        iGraphableElements.removeAllElements();
    }

    private JComponent getIceLogoComponent() {
        JLabel lIceLogoImage = new JLabel("Welcome to iceLogo!");

        try {
            URL lURL = this.getClass().getClassLoader().getResource("icons/iceLogo.jpg");
            lIceLogoImage = new JLabel(new ImageIcon(lURL));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lIceLogoImage;
    }

    private void createMenu() {
        menubar = new JMenuBar();

        JMenuItem item;
        JMenu main = new JMenu("Main");

        item = new JMenuItem("About");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                showAboutDialog();
            }
        });

        main.add(item);

        item = new JMenuItem("Exit");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                close();
            }
        });
        main.add(item);
        menubar.add(main);

        JMenu mode = new JMenu("Mode");


        mode.add(item);

        menubar.add(mode);

        this.setJMenuBar(menubar);


    }

    private void showAboutDialog() {

        JTextArea txa = new JTextArea(getAboutContent());
        txa.setLineWrap(true);
        txa.setColumns(50);

        JPanel jpanContent = new JPanel(new BorderLayout(0, 10));
        jpanContent.add(this.getIceLogoComponent(), BorderLayout.NORTH);
        jpanContent.add(txa);

        JScrollPane scroll = new JScrollPane(jpanContent, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JDialog dialog = new JDialog(this, "About IceLogo - " + ICELOGO_VERSION);
        dialog.add(scroll);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void close() {
        // Persist properties into icelogo.properties.
        MainInformationFeeder.getInstance().storeProperties();
        int lOption = JOptionPane.showConfirmDialog(IceLogo.this, "Please confirm exit.");
        if (lOption == JOptionPane.OK_OPTION) {
            System.out.println("Exiting .. ");
            System.exit(0);
        } else {
            System.out.println("Exit cancelled .. ");
        }
    }

    /**
     * This method extracts the last version from the 'about.txt' file.
     *
     * @return String with the String of the latest version, or '
     */

    public String getLastVersion() {

        String lAboutContent = getAboutContent();

        String result = null;

        int start = lAboutContent.lastIndexOf("- Version ") + 10;
        int end = lAboutContent.indexOf("\n", start);
        if (start > 0 && end > 0) {
            result = lAboutContent.substring(start, end).trim();
        } else {
            result = "";
        }

        return result;
    }


    public static void main(final String[] args) {
        new IceLogo("LoGo");
    }

    private String getAboutContent() {
        String lFile = "about.txt";
        String lContent = null;
        try {
            // First of all, try it via the classloader for this file.
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(lFile);
            if (is == null) {
                // Apparently not found, try again with the System (bootstrap) classloader.
                is = ClassLoader.getSystemResourceAsStream(lFile);
                if (is == null) {
                    lContent = "No help file (" + lFile + ") could be found in the classpath!";
                }
            }

            // See if we have an input stream.
            if (is != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                lContent = sb.toString();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return lContent;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        jpanContent = new JPanel();
        jpanContent.setLayout(new GridBagLayout());
        jpanContent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null));
        final JPanel spacer1 = new JPanel();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpanContent.add(spacer1, gbc);
        jpanStatus = new JPanel();
        jpanStatus.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        jpanContent.add(jpanStatus, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpanStatus.add(spacer2, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 3;
        gbc.ipady = 3;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanStatus.add(jpanMessage, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("p-value");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 3, 0, 3);
        jpanStatus.add(label1, gbc);
        spinPValue.setToolTipText("Enter a general p-value");
        gbc = new GridBagConstraints();
        gbc.gridx = 8;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 20;
        gbc.insets = new Insets(0, 5, 0, 5);
        jpanStatus.add(spinPValue, gbc);
        final JToolBar toolBar1 = new JToolBar();
        toolBar1.setMargin(new Insets(10, 0, 10, 0));
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanStatus.add(toolBar1, gbc);
        toolBar1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null));
        btnColor.setMargin(new Insets(10, 5, 10, 5));
        btnColor.setText("");
        btnColor.setToolTipText("Logo color chooser");
        toolBar1.add(btnColor);
        btnParam.setMargin(new Insets(10, 5, 10, 5));
        btnParam.setText("");
        btnParam.setToolTipText("Logo parameters");
        toolBar1.add(btnParam);
        btnAaParam.setMargin(new Insets(10, 5, 10, 5));
        btnAaParam.setText("");
        btnAaParam.setToolTipText("Aa parameter matrix chooser");
        toolBar1.add(btnAaParam);
        addSpecies.setMargin(new Insets(10, 5, 10, 5));
        addSpecies.setToolTipText("Add a the AA composition of specific species to iceLogo");
        toolBar1.add(addSpecies);
        iceLogoRadioButton = new JRadioButton();
        iceLogoRadioButton.setSelected(true);
        iceLogoRadioButton.setText("iceLogo");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 5, 2, 5);
        jpanStatus.add(iceLogoRadioButton, gbc);
        heatmapRadioButton = new JRadioButton();
        heatmapRadioButton.setSelected(false);
        heatmapRadioButton.setText("Heatmap");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 5, 8, 5);
        jpanStatus.add(heatmapRadioButton, gbc);
        barchartRadioButton = new JRadioButton();
        barchartRadioButton.setSelected(true);
        barchartRadioButton.setText("Barchart");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 5, 2, 5);
        jpanStatus.add(barchartRadioButton, gbc);
        sublogoRadioButton = new JRadioButton();
        sublogoRadioButton.setText("Sublogo");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 5, 8, 5);
        jpanStatus.add(sublogoRadioButton, gbc);
        sequenceLogoRadioButton = new JRadioButton();
        sequenceLogoRadioButton.setText("Sequence logo");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 5, 2, 5);
        jpanStatus.add(sequenceLogoRadioButton, gbc);
        AAParameterMatrixRadioButton = new JRadioButton();
        AAParameterMatrixRadioButton.setText("AA parameter matrix");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 5, 8, 5);
        jpanStatus.add(AAParameterMatrixRadioButton, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpanStatus.add(mainProgress, gbc);
        conservationLogoRadioButton = new JRadioButton();
        conservationLogoRadioButton.setText("Conservation line");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 5, 2, 5);
        jpanStatus.add(conservationLogoRadioButton, gbc);
        tlbIcons.setMargin(new Insets(10, 0, 10, 0));
        tlbIcons.setOrientation(1);
        tlbIcons.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(tlbIcons, gbc);
        tlbIcons.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null));
        btnSampling.setMargin(new Insets(10, 5, 10, 5));
        btnSampling.setText("");
        btnSampling.setToolTipText("New one sample experiment");
        tlbIcons.add(btnSampling);
        btnStatic.setMargin(new Insets(10, 5, 10, 5));
        btnStatic.setText("");
        btnStatic.setToolTipText("New static experiment");
        tlbIcons.add(btnStatic);
        tabPanel = new JTabbedPane();
        tabPanel.setTabLayoutPolicy(0);
        tabPanel.setTabPlacement(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 0, 0);
        jpanContent.add(tabPanel, gbc);
        tabPanel.addTab("Intro", jpanMain);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(0, 0, 10, 0);
        jpanContent.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        jpanContent.add(spacer4, gbc);
        final JToolBar toolBar2 = new JToolBar();
        toolBar2.setMargin(new Insets(0, 0, 0, 0));
        toolBar2.setOrientation(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTH;
        jpanContent.add(toolBar2, gbc);
        toolBar2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null));
        iSaveToPdfButton.setMargin(new Insets(10, 5, 10, 5));
        iSaveToPdfButton.setText("");
        iSaveToPdfButton.setToolTipText("Save current analysis to pdf");
        toolBar2.add(iSaveToPdfButton);
        btnClose.setMargin(new Insets(10, 5, 10, 5));
        btnClose.setText("");
        btnClose.putClientProperty("html.disable", Boolean.FALSE);
        toolBar2.add(btnClose);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanContent;
    }
}