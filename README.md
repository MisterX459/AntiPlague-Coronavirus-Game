AntiPlague Coronavirus Game
AntiPlague Coronavirus Game is a Java-based simulation inspired by "Plague Inc." where the player works to prevent a global virus outbreak. The game displays a world map divided into at least 10 countries, with multiple transport connections (such as airlines, buses, and boats) that influence the virus spread. Each connection has customizable criteria that can be modified by purchasing upgrades.

Key Features:

Graphical User Interface (GUI):
Developed using the Swing framework (no JavaFX), the application features multiple windows for the main menu, game play, difficulty selection, and high scores. The high score list is implemented using a JList component with scrollbar support for large records.

Real-Time Game Mechanics:
The game utilizes separate threads for managing the time counter and virus behavior. A dedicated keyboard shortcut (Ctrl+Shift+Q) allows players to interrupt gameplay and return to the main menu at any time.

Dynamic Gameplay:
With at least three difficulty levels, the virus spreads progressively across countries via animated transport routes. Each country's transport connections have unique, upgradeable criteria that affect infection dynamics.

Upgrades & Scoring:
Players can earn points by saving or curing individuals and invest these points in one of at least nine upgrades to improve transport criteria and slow down the virus. High scores are calculated based on time, in-game achievements, and selected difficulty, and are persistently saved using the Serializable interface.

Robust Code Structure:
The project follows the MVC design pattern to separate game logic, user interface, and data management. Advanced object-oriented programming techniques are used throughout:

Inheritance & Interfaces: For creating flexible and reusable components.
Collections & Generics: To manage dynamic sets of countries, upgrades, and scores.
Lambda Expressions: For concise event handling.
Exception Handling: Ensures that any errors are gracefully reported to the user.
This project offers an engaging, well-structured simulation experience where strategic upgrades and quick decisions are key to saving the world from a global pandemic.
