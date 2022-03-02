# ShoppingList

This is a shopping list implemented with Kotlin in Android Studio.

Implemented features:
- application starts with a Splash Activity that displays a custom logo and jumps to the Shopping List after 3 seconds.
- An Item have the attributes category(names such as food, electronic, book, etc., and the corresponding icon of the item in the list depends on it’s category), name, description, estimated price, and the status (true/false weather it has been bought yet or not).
- The Shopping List displays the icon for the item (an ImageView based on the Category), checkbox weather it has been bought or not (user can change it during shopping), name of the item, additional attributes can also be displayed of course!
- “New Item” menu in the Toolbar that navigates to a New Item Dialog or Activity, where the user can pick up new items that appear on the Shopping List Activity.
- removes items in two ways: one-by one and all items at the same time (“Delete all” menu in the Toolbar).
- supports editing Items.
- Uses database/persistence data storage for storing the items (i.e. Room)
-“View Item Details” button that shows the item with all details on a Dialog or new Activity.


Reference:
Ekler, Péter (2021) AIT2021Fall/KotlinFileDemo [Source Code] https://github.com/peekler/AIT2021Fall/tree/main/KotlinFileDemo
