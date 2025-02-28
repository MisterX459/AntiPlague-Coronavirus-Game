

    import controller.MainMenuController;
    import model.GameModel;
    import view.MainMenuView;

    import javax.swing.SwingUtilities;

    /**
     * Main class that starts the application.
     */

    public class AntiPlagueGame {
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                // 1) Create the Model (where we will store the chosen difficulty).
                GameModel model = new GameModel();

                // 2) Create the Main Menu view.
                MainMenuView mainMenuView = new MainMenuView();
                mainMenuView.setVisible(true);
                // 3) Create the Main Menu controller, linking model and view.
                MainMenuController mainMenuController = new MainMenuController(model, mainMenuView);

                // 4) Show the Main Menu.
                mainMenuView.setVisible(true);

            });
        }
    }