package com.example;


import java.sql.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.util.ArrayList;

/**
 * Represents the MAIN GUI class that is responsible for running all tabs and delegating responsiblities to the children classes
 * @author Haresh Raj, Harsh Dave
 */
public class GUI extends JFrame {
  static JFrame f;
  /**
   * Cumulative declaration of all main panels
   */
  private JPanel mainPanel, menuPanel, sidePanel, drinksPanel, extrasPanel, rightPanel, sizePanel, cardPanel;
  /**
   * Cumulative declaration of all main buttons
   */
  private JButton userSwitchButton, inventoryButton, employeesButton, menuButton, addButton, orderButton, totalLabel;

  /**
   * Connection to the database
   */
  private Connection conn;
  /**
   * Order details
   */
  private OrderDetails totalOrder;
  private static Login login = new Login();

  // each following array is used to store all of the buttons so that they can be
  // reset on invalid input or order completion

  /**
   * Stores all entree buttons
   */
  private ArrayList<EntreeButtonListener> entreeButtons = new ArrayList<>();
  /**
   * Stores all side buttons
   */
  private ArrayList<SideButtonListener> sideButtons = new ArrayList<>();
  /**
   * Stores all extras buttons
   */
  private ArrayList<ExtrasButtonListener> extraButtons = new ArrayList<>();
  /**
   * Stores all drink buttons
   */
  private ArrayList<DrinksButtonListener> drinkButtons = new ArrayList<>();
  /**
   * Used to reset the isFamily() boolean
   */
  private FamilyButtonListener familyButton;
  /**
   * Used to reset the isKid() boolean
   */
  private KidButtonListener kidButton;

  /**
   * Stores each order to be edited
   */
  private Order order = new Order();

  /**
   * Card layout to create tabs
   */
  private CardLayout cardLayout;

  /**
   * Constructor
   */
  public GUI() {}

  /**
   * This is our main function which is responsible for running the entire implementation from command line
   * @param args command line arguments
   */
  public static void main(String[] args) {

    // this snippet ensuring that styling is consistent across all machines
    try {
      for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
          if ("Nimbus".equals(info.getName())) {
              UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // initializes database connection to be used throughout the program
    Connection conn = setupDatabaseConnection();

    // initializes the inventory manager to communicate with the database
    InventoryManager runningInventory = new InventoryManager(conn);
    
    
    // initialize the GUI, inventory page, and order details
    GUI gui = new GUI();
    InventoryPage iPage = new InventoryPage(gui, conn);
    OrderDetails totalOrder = new OrderDetails(conn, runningInventory, gui, iPage);

    
    if (conn == null){
      return;
    }

    // establishing the main GUI
    f = new JFrame("DB GUI");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setExtendedState(JFrame.MAXIMIZED_BOTH);

    gui.conn = conn;
    gui.totalOrder = totalOrder; 
    gui.initializeCardLayout();
    gui.createMainPanel(conn, totalOrder, iPage);

    f.add(gui.cardPanel);
    f.setVisible(true);
    f.setEnabled(false); // disable the main frame until the user logs in
    
    gui.runPinPad();
  }


  /**
   * This function establishes the database connection and allows us to pull any information from our many tables
   */
  private static Connection setupDatabaseConnection() {
    Connection conn = null;

    // Change to "postgres" if using PostgreSQL
    String dbType = "postgres";
    try {
      if (dbType.equals("postgres")) {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu:5432/csce331_44", "csce331_44", "cowboys44");
      } 
      else if (dbType.equals("sqlite")) {
        Class.forName("org.sqlite.JDBC");

        // Edit to your local path
        conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\rumme\\Desktop\\School\\Software Engineering\\Panda\\project-2-44-cowboys\\panda.db/");
      }
    } 
    catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
    return conn;
  }


  /**
   * This is a small helper function that initializes the tabs
   */
  private void initializeCardLayout() {
    cardLayout = new CardLayout();
    cardPanel = new JPanel(cardLayout);
  }


  /**
   * This function is intended to create the pan els that will allow for organization of all buttons into defined and editable sections.
   * @param conn connection to database
   * @param totalOrder order details
   */
  private void createMainPanel(Connection conn, OrderDetails totalOrder, InventoryPage iPage) {
    mainPanel = new JPanel(new BorderLayout());

    // section headings (includes font and styling)

    Font buttonFont = new Font("Arial", Font.PLAIN, 18);

    menuPanel = new JPanel(new GridLayout(4, 4));
    menuPanel.setBorder(BorderFactory.createTitledBorder("Entrees"));

    menuPanel.setBorder(BorderFactory.createTitledBorder(null, "Entrees", TitledBorder.CENTER, TitledBorder.TOP, buttonFont));

    JPanel leftPanel = new JPanel(new GridLayout(4, 1));

    sizePanel = new JPanel(new GridLayout(0, 2));
    sizePanel.setBorder(BorderFactory.createTitledBorder("Sizes"));

    sizePanel.setBorder(BorderFactory.createTitledBorder(null, "Sizes", TitledBorder.CENTER, TitledBorder.TOP, buttonFont));

    // size button formatting (kid and family options)
    JButton button = new JButton("Kid");
    button.setBorder(new LineBorder(Color.GRAY, 1));
    kidButton = new KidButtonListener(order);
    button.addActionListener(kidButton);
    sizePanel.add(button);

    button = new JButton("Family");
    button.setBorder(new LineBorder(Color.GRAY, 1));
    familyButton = new FamilyButtonListener(order);
    button.addActionListener(familyButton);
    sizePanel.add(button);

    // build the different panel structures
    sidePanel = new JPanel(new GridLayout(0, 2));
    sidePanel.setBorder(BorderFactory.createTitledBorder("Sides"));

    sidePanel.setBorder(BorderFactory.createTitledBorder(null, "Sides", TitledBorder.CENTER, TitledBorder.TOP, buttonFont));

    extrasPanel = new JPanel(new GridLayout(0, 2));
    extrasPanel.setBorder(BorderFactory.createTitledBorder("Extras"));

    extrasPanel.setBorder(BorderFactory.createTitledBorder(null, "Extras", TitledBorder.CENTER, TitledBorder.TOP, buttonFont));

    drinksPanel = new JPanel(new GridLayout(0, 2));
    drinksPanel.setBorder(BorderFactory.createTitledBorder("Drinks"));

    drinksPanel.setBorder(BorderFactory.createTitledBorder(null, "Drinks", TitledBorder.CENTER, TitledBorder.TOP, buttonFont));

    leftPanel.add(sizePanel);
    leftPanel.add(sidePanel);
    leftPanel.add(extrasPanel);
    leftPanel.add(drinksPanel);

    addMenuButtons(menuPanel, sidePanel, drinksPanel, extrasPanel, conn);

    rightPanel = new JPanel();
    rightPanel.setLayout(new GridLayout(2, 1));

    // formatting for ORDER button
    orderButton = new JButton("ORDER");
    orderButton.setBackground(new Color(0, 76, 153));
    orderButton.setForeground(Color.WHITE);
    orderButton.addActionListener(new OrderButtonListener(totalOrder));
    rightPanel.add(orderButton, BorderLayout.NORTH);

    // formatting for ADD button
    addButton = new JButton("+");
    addButton.setFont(new Font("Arial", Font.PLAIN, 40));
    addButton.setBackground(new Color(8, 101, 8));
    addButton.setForeground(Color.WHITE);
    addButton.addActionListener(new AddButtonListener(order, this, totalOrder));
    rightPanel.add(addButton, BorderLayout.SOUTH);

    // formatting for the top bar
    JPanel topPanel = new JPanel(new GridLayout(1, 3));
    userSwitchButton = new JButton("Switch User");
    inventoryButton = new JButton("Inventory");
    employeesButton = new JButton("Employees");
    menuButton = new JButton("Menu");
    // Login login = new Login();
    userSwitchButton.addActionListener(e -> runPinPad());

    // this allows us to lock non-managers out of certain areas
    inventoryButton.setEnabled(false);
    employeesButton.setEnabled(false);
    menuButton.setEnabled(false);

    employeesButton.addActionListener(e -> switchToEmployeesPage());
    inventoryButton.addActionListener(e -> switchToInventoryPage());
    menuButton.addActionListener(e -> switchToMenuPage());


    // we must add all buttons
    topPanel.add(userSwitchButton);
    topPanel.add(inventoryButton);
    topPanel.add(menuButton);
    topPanel.add(employeesButton);
    mainPanel.add(topPanel, BorderLayout.NORTH);
    mainPanel.add(menuPanel, BorderLayout.CENTER);
    mainPanel.add(leftPanel, BorderLayout.WEST);
    mainPanel.add(rightPanel, BorderLayout.EAST);

    cardPanel.add(mainPanel, "mainPanel");
    cardPanel.add(new EmployeesPage(this, conn), "employeesPage");
    cardPanel.add(new MenuPage(this, conn), "menuPage");
    cardPanel.add(iPage, "inventoryPage");
  }


    /**
     * This is a helper function for the above function that allows for all buttons to be added to their respective panels
     * @param menuPanel main menu panel
     * @param sidePanel side panel
     * @param drinksPanel drinks panel
     * @param extrasPanel extras panel
     * @param conn connection to database
     */
    private void addMenuButtons(JPanel menuPanel, JPanel sidePanel, JPanel drinksPanel, JPanel extrasPanel, Connection conn) {
      try {
        // exectuable SQL statement
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery("SELECT product_name, type FROM menu");

        while (result.next()) {
          String productName = result.getString("product_name");
          String type = result.getString("type");

          if (type.equals("entree") && productName.substring(0, 2).equals("L_")) {
            // create a button and format it
            JButton button = new JButton(productName.substring(2));
            button.setBorder(new LineBorder(Color.GRAY, 1));

            // add the button to the array
            EntreeButtonListener newButton = new EntreeButtonListener(order);
            entreeButtons.add(newButton);
            button.addActionListener(newButton);

            // add the button to the menu
            menuPanel.add(button);

          } 
          else if (type.equals("side") && productName.substring(0, 2).equals("L_")) {
            // create a button and format it
            JButton button = new JButton(productName.substring(2));
            button.setBorder(new LineBorder(Color.GRAY, 1));

            // add the button to the array
            SideButtonListener newButton = new SideButtonListener(order);
            sideButtons.add(newButton);
            button.addActionListener(newButton);

            // add the button to the menu
            sidePanel.add(button);
          } 
          else if (type.equals("appetizer") && productName.substring(0, 2).equals("L_")) {
            // create a button and format it
            JButton button = new JButton(productName.substring(2));
            button.setBorder(new LineBorder(Color.GRAY, 1));

            // add the button to the array
            ExtrasButtonListener newButton = new ExtrasButtonListener(order);
            extraButtons.add(newButton);
            button.addActionListener(newButton);

            // add the button to the menu
            extrasPanel.add(button);
          } 
          else if (type.equals("drink") && !productName.substring(0, 2).equals("S_") && !productName.substring(0, 2).equals("M_")) {
            // create a button and format it
            JButton button = new JButton(productName.substring(2));
            button.setBorder(new LineBorder(Color.GRAY, 1));

            // add the button to the array
            DrinksButtonListener newButton = new DrinksButtonListener(order);
            drinkButtons.add(newButton);
            button.addActionListener(newButton);

            // add the button to the menu
            drinksPanel.add(button);
          }
        }

        // place holder for displaying current order total
        totalLabel = new JButton("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel totalPanel = new JPanel(new GridBagLayout());
        totalPanel.add(totalLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        totalPanel.add(totalLabel, gbc);
        menuPanel.add(totalPanel, BorderLayout.SOUTH);

      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error accessing Database.");
      }
  }
  
  
  /**
   * This function is responsible for refreshing the menu panels by clearing the existing buttons and adding new ones
   * @param conn connection to database
  */
  public void updateMenuPanels(Connection conn) {
    clearMenuPanels();
    addMenuButtons(menuPanel, sidePanel, drinksPanel, extrasPanel, conn);
    revalidate();
    repaint();
  }

  
  /**
   * This function is responsible for clearing the menu panels by removing all buttons and clearing the lists
  */
  private void clearMenuPanels() {
    menuPanel.removeAll();
    sidePanel.removeAll();
    drinksPanel.removeAll();
    extrasPanel.removeAll();

    entreeButtons.clear();
    sideButtons.clear();
    extraButtons.clear();
    drinkButtons.clear();
  }

  
  /**
   * Responsible for clearing the current GUI interface in order for new input to be placed after either bad input or the completion/payment
   * @param order current order
   */
  public void resetGUI(Order order) {
    resetButtonBorders(sizePanel);
    resetButtonBorders(sidePanel);
    resetButtonBorders(extrasPanel);
    resetButtonBorders(drinksPanel);
    resetButtonBorders(menuPanel);

    // clear entrees
    for (EntreeButtonListener item : entreeButtons) {
      item.clearCounts();
    }
    order.updateEntreeCount(-order.getEntreeCount());

    // clear sides
    for (SideButtonListener item : sideButtons) {
      item.clearCounts();
    }
    order.updateSideCount(-order.getSideCount());

    // clear extras
    for (ExtrasButtonListener item : extraButtons) {
      item.clearCounts();
    }
    order.updateExtraCount(-order.getExtraCount());

    // clear drinks
    for (DrinksButtonListener item : drinkButtons) {
      item.clearCounts();
    }
    order.updateDrinkCount(-order.getDrinkCount());

    // reset kid boolean
    kidButton.clearCounts();
    order.updateKid(false);

    // reset family boolean
    familyButton.clearCounts();
    order.updateFamily(false);

    // call destructors
    order.getEntree().clear();
    order.getSide().clear();
    order.getExtra().clear();
    order.getDrink().clear();

    // reinitialize order
    order = new Order();
  }

  
  /**
   * This is a helper function for reseting the GUI buttons (assists resetGUI())
   * @param panel panel to reset
   */
  private void resetButtonBorders(JPanel panel) {
    for (Component comp : panel.getComponents()) {
      if (comp instanceof JButton) {
        JButton button = (JButton) comp;
        button.setBorder(new LineBorder(Color.GRAY, 1));
        button.setEnabled(true);
      }
    }
  }

  
  /**
   * This is a set of functions to switch between the different tabs or "cards"
   */
  public void switchToMainPage() {
    cardLayout.show(cardPanel, "mainPanel");
  }

  private void switchToEmployeesPage() {
    cardLayout.show(cardPanel, "employeesPage");
  }

  private void switchToInventoryPage() {
    cardLayout.show(cardPanel, "inventoryPage");
  }

  private void switchToMenuPage() {
    cardLayout.show(cardPanel, "menuPage");
  }

  /**
   * This function is responsible for updating the total label to reflect the current order total
   */
  public void updateTotalLabel() {
    Total total = new Total(totalOrder, conn);
    totalLabel.setText("Total: $" + String.format("%.2f", total.getTotal()));
  }


  /**
   * This method allows switching to any page
   * @param pageName name of the page to switch to
   */
  public void switchToPage(String pageName) {
    cardLayout.show(cardPanel, pageName);
  }


  /**
   * Allows adding new pages dynamically (for OrderHistory)
   * @param page page to add
   * @param name name of the page
   */
  public void addPage(JPanel page, String name) {
    cardPanel.add(page, name);
  }

  /**
   * This function is responsible for returning the login object
   * @return login object
   */
  public Login getLogin() {
    return login;
  }

  /**
   * This function is responsible for running the pinpad to allow for user login
   * It also logs out of the current user if the switch user button is pressed
   */
  public void runPinPad() {    
    login.setEmployeeName("");
    while (login.getEmployeeName().equals("")) {
      login.pinpad(conn, inventoryButton, menuButton, employeesButton, f);      
    }
  }
}
