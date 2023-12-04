import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PersonCrud {

    private JFrame mainFrame;
    private DefaultListModel<Person> listModel;
    private JList<Person> personList;
    private ArrayList<Person> people;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PersonCrud app = new PersonCrud();
            app.createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        mainFrame = new JFrame("Person CRUD Application");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        listModel = new DefaultListModel<>();
        personList = new JList<>(listModel);
        personList.setCellRenderer(new PersonCellRenderer());

        JScrollPane listScrollPane = new JScrollPane(personList);

        JButton addButton = new JButton(new ImageIcon("add.png"));
        addButton.addActionListener(e -> addPerson());

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton(new ImageIcon("search.png"));
        searchButton.addActionListener(e -> searchPerson(searchField.getText()));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(searchField);
        buttonPanel.add(searchButton);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(listScrollPane, BorderLayout.CENTER);
        mainFrame.add(buttonPanel, BorderLayout.SOUTH);

        mainFrame.setPreferredSize(new Dimension(400, 300));
        mainFrame.pack();
        mainFrame.setVisible(true);

        people = new ArrayList<>();
    }

    private void addPerson() {
        JFrame addFrame = new JFrame("AGREGAR PERSONAS");
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel idLabel = new JLabel("CEDULA:");
        JTextField idField = new JTextField(10);
        JLabel nameLabel = new JLabel("NOMBRE:");
        JTextField nameField = new JTextField(20);
        JLabel lastNameLabel = new JLabel("APELLIDO:");
        JTextField lastNameField = new JTextField(20);
        JButton saveButton = new JButton(new ImageIcon("save.png"));
        saveButton.addActionListener(e -> {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String lastName = lastNameField.getText();

            Person person = new Person(id, name, lastName);
            listModel.addElement(person);
            
            ListaSimple lista = new ListaSimple();
            lista.insertarUltimo(person);

            addFrame.dispose();
        });
        

        JPanel addPanel = new JPanel();
        addPanel.setLayout(new GridLayout(4, 2));
        addPanel.add(idLabel);
        addPanel.add(idField);
        addPanel.add(nameLabel);
        addPanel.add(nameField);
        addPanel.add(lastNameLabel);
        addPanel.add(lastNameField);
        addPanel.add(saveButton);

        addFrame.add(addPanel);
        addFrame.setPreferredSize(new Dimension(300, 150));
        addFrame.pack();
        addFrame.setVisible(true);
    }

    private void editPerson(Person person) {
        ListaSimple lista = new ListaSimple();
        lista.actualizar(person);
    }

    private void searchPerson(String searchTerm) {
        listModel.clear();
        for (Person person : people) {
            if (person.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                person.getLastName().toLowerCase().contains(searchTerm.toLowerCase())) {
                listModel.addElement(person);
            }
        }
    }

    private class Person {
        private int id;
        private String name;
        private String lastName;

        public Person(int id, String name, String lastName) {
            this.id = id;
            this.name = name;
            this.lastName = lastName;
        }

        public String getName() {
            return name;
        }

        public String getLastName() {
            return lastName;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + name + ", Last Name: " + lastName;
        }
    }

    private class PersonCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            JButton editButton = new JButton(new ImageIcon("edit.png"));
            editButton.addActionListener(e -> editPerson((Person) value));

            JButton deleteButton = new JButton(new ImageIcon("delete.png"));
            deleteButton.addActionListener(e -> {
            	int selectedIndex = personList.getSelectedIndex();
            	Person person = listModel.get(selectedIndex);
                if (selectedIndex != -1) {
                	people.remove(person);
                    listModel.remove(selectedIndex);
                }
                ListaSimple lista = new ListaSimple();
                lista.eliminar(person);
            });

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(this, BorderLayout.CENTER);
            panel.add(editButton, BorderLayout.EAST);
            panel.add(deleteButton, BorderLayout.WEST);
            return panel;
        }
    }
}