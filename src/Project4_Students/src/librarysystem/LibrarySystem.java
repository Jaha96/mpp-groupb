package librarysystem;

import business.ControllerInterface;
import business.SystemController;
import components.OverduePanel;
import dataaccess.Auth;
import librarysystem.checkout.BookCheckoutWindow;
import components.BookManagerPanel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


public class LibrarySystem extends JFrame implements LibWindow {

  ControllerInterface ci = SystemController.INSTANCE;
  public final static LibrarySystem INSTANCE = new LibrarySystem();
  private static JPanel mainPanel = new JPanel();
  JMenuBar menuBar;
  JMenu options,eLib;
  JMenuItem login, allBookIds, allMemberIds, addMember, home, addBook, checkOutBook, printCheckout, overdue;
  String pathToImage;
  private boolean isInitialized = false;

  private static JPanel[] allPanels = {
      OverduePanel.INSTANCE,
      BookManagerPanel.INSTANCE,
      mainPanel
  };

  private static LibWindow[] allWindows = {
      LibrarySystem.INSTANCE,
      LoginWindow.INSTANCE,
      AllMemberIdsWindow.INSTANCE,
      AllBookIdsWindow.INSTANCE,
      AddMemberWindow.INSTANCE,
  };

  public static void hideAllWindows() {
    for (LibWindow frame : allWindows) {
      frame.setVisible(false);
    }
  }

  private LibrarySystem() {
  }

  public void init() {
    formatContentPane();
    setPathToImage();
    insertSplashImage();
    createMenus();
    //pack();
    setSize(660, 500);
    isInitialized = true;
  }

  private void formatContentPane() {
    mainPanel.setLayout(new GridLayout(1, 1));
    LibrarySystem.INSTANCE.getContentPane().add(mainPanel);
  }

  private void setPathToImage() {
    String currDirectory = System.getProperty("user.dir");
    pathToImage = currDirectory + "/src/librarysystem/library.jpg";
  }

  private void insertSplashImage() {
    ImageIcon image = new ImageIcon(pathToImage);
    mainPanel.add(new JLabel(image));
  }

  private void createMenus() {
    menuBar = new JMenuBar();
    menuBar.setBorder(BorderFactory.createRaisedBevelBorder());
    addMenuItems();
    setJMenuBar(menuBar);
  }

  private void addMenuItems() {
    eLib = new JMenu("eLib");
    options = new JMenu("Options");
    menuBar.add(eLib);
    menuBar.add(options);

    addHomeMenuItem(eLib);
    addLoginMenuItem(eLib);
    addViewAllBookIdsMenuItem(options);
    addViewAllMemberIdsMenuItem(options);
    addAddMemberMenuItem(options);
    checkoutBookMenuItem(options);
    printCheckoutMenuItem(options);
    overdueMenuItem(options);
  }

  private void addHomeMenuItem(JMenu menus) {
    home = new JMenuItem("Home");
    home.addActionListener(new HomeListener());
    menus.add(home);
  }
  private void addAddMemberMenuItem(JMenu options) {
    this.addMember = new JMenuItem("Add Member");
    if (SystemController.currentAuth == Auth.LIBRARIAN) {
      this.addMember.setEnabled(false);
    }
    this.addMember.addActionListener(new AddMemberListener());
    options.add(this.addMember);
  }



  private void checkoutBookMenuItem(JMenu options) {
    this.checkOutBook = new JMenuItem("Checkout Book");
    if (SystemController.currentAuth == Auth.ADMIN) {
      this.checkOutBook.setEnabled(false);
    }
    this.checkOutBook.addActionListener(new CheckoutBookListener());
    options.add(this.checkOutBook);
  }


  private void printCheckoutMenuItem(JMenu options) {
    this.addMember = new JMenuItem("Print Checkout");
    if (SystemController.currentAuth == Auth.ADMIN) {
      this.addMember.setEnabled(false);
    }
    this.addMember.addActionListener(new PrintCheckoutListener());
    options.add(this.addMember);
  }

  private void overdueMenuItem(JMenu options) {
    this.overdue = new JMenuItem("Overdue");
    if (SystemController.currentAuth == Auth.ADMIN) {
      this.overdue.setEnabled(false);
    }
    this.overdue.addActionListener(new OverdueListener());
    options.add(this.overdue);
  }


  private void addViewAllMemberIdsMenuItem(JMenu options) {
    allMemberIds = new JMenuItem("All Member Ids");
    if (SystemController.currentAuth == Auth.LIBRARIAN) {
      this.allMemberIds.setEnabled(false);
    }
    allMemberIds.addActionListener(new AllMemberIdsListener());
    options.add(allMemberIds);
  }

  private void addViewAllBookIdsMenuItem(JMenu options) {
    allBookIds = new JMenuItem("All Books");
    if (SystemController.currentAuth == Auth.LIBRARIAN) {
      allBookIds.setEnabled(false);
    }
    allBookIds.addActionListener(new AllBookIdsListener());
    options.add(allBookIds);
  }

  private void addLoginMenuItem(JMenu options) {
    if (SystemController.currentAuth != null) {
      login = new JMenuItem("Logout");
      login.addActionListener(new LogoutListener());
      options.add(login);
    } else {
      login = new JMenuItem("Login");
      login.addActionListener(new LoginListener());
      options.add(login);
    }
  }

  class LogoutListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      SystemController.INSTANCE.currentAuth = null;
      LibrarySystem.hideAllWindows();
      removeAllPanels();
      LoginWindow.INSTANCE.init();
      Util.centerFrameOnDesktop(LoginWindow.INSTANCE);
      LoginWindow.INSTANCE.setVisible(true);
    }
  }

  class LoginListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      LibrarySystem.hideAllWindows();
      LoginWindow.INSTANCE.init();
      Util.centerFrameOnDesktop(LoginWindow.INSTANCE);
      LoginWindow.INSTANCE.setVisible(true);

    }

  }

  class HomeListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      removeAllPanels();
      LibrarySystem.INSTANCE.getContentPane().add(mainPanel);
      pack();
      repaint();
    }

  }

  class AllBookIdsListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      BookManagerPanel.INSTANCE.init();
      setMainPanel(BookManagerPanel.INSTANCE);
    }

  }

  class AddMemberListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      LibrarySystem.hideAllWindows();
      AddMemberWindow.INSTANCE.init();
      AddMemberWindow.INSTANCE.pack();
      Util.centerFrameOnDesktop(AddMemberWindow.INSTANCE);
      AddMemberWindow.INSTANCE.setVisible(true);
    }
  }

  private void setMainPanel(JComponent jPanel) {
    removeAllPanels();
    LibrarySystem.INSTANCE.getContentPane().add(jPanel);
    revalidate(); // Refresh the frame to show the new panel
    repaint();
    pack();
  }


  class CheckoutBookListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      LibrarySystem.hideAllWindows();
      BookCheckoutWindow.INSTANCE.init();
      BookCheckoutWindow.INSTANCE.pack();
      Util.centerFrameOnDesktop(BookCheckoutWindow.INSTANCE);
      BookCheckoutWindow.INSTANCE.setVisible(true);
    }
  }

  class PrintCheckoutListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      LibrarySystem.hideAllWindows();
      PrintCheckoutWindow.INSTANCE.init();
      PrintCheckoutWindow.INSTANCE.pack();
      Util.centerFrameOnDesktop(PrintCheckoutWindow.INSTANCE);
      PrintCheckoutWindow.INSTANCE.setVisible(true);
    }
  }

  class OverdueListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      OverduePanel.INSTANCE.init();
      setMainPanel(OverduePanel.INSTANCE);
    }
  }

  class AllMemberIdsListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      LibrarySystem.hideAllWindows();
      AllMemberIdsWindow.INSTANCE.init();
      AllMemberIdsWindow.INSTANCE.pack();
      AllMemberIdsWindow.INSTANCE.setVisible(true);

      LibrarySystem.hideAllWindows();
//      AllBookIdsWindow.INSTANCE.init();

      List<String> ids = ci.allMemberIds();
      Collections.sort(ids);
      StringBuilder sb = new StringBuilder();
      for (String s : ids) {
        sb.append(s + "\n");
      }
      System.out.println(sb.toString());
      AllMemberIdsWindow.INSTANCE.setData(sb.toString());
      AllMemberIdsWindow.INSTANCE.pack();
      //AllMemberIdsWindow.INSTANCE.setSize(660,500);
      Util.centerFrameOnDesktop(AllMemberIdsWindow.INSTANCE);
      AllMemberIdsWindow.INSTANCE.setVisible(true);
    }

  }

  @Override
  public boolean isInitialized() {
    return isInitialized;
  }


  @Override
  public void isInitialized(boolean val) {

  }

  private void removeAllPanels() {
    for (JPanel panel : allPanels) {
      LibrarySystem.INSTANCE.getContentPane().remove(panel);
    }
  }

}
