/**
 * FXMLMateriasController es la clase que posee los elementos
 * de la ventana, asi como sus respectivas acciones que permiten
 * llevar el control de una tabla de materias.
 *
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/03/21
 */
package alumnos.controlador;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import alumnos.IMateriaDAO;
import alumnos.Materia;
import alumnos.MateriaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLMateriasController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button agregarButton;

	@FXML
	private Button cargarBDButton;

	@FXML
	private TableColumn<Materia, Integer> creditosTableColumn;

	@FXML
	private TextField creditosTextField;

	@FXML
	private Button editarButton;

	@FXML
	private Button eliminarButton;

	@FXML
	private Button guardarBDButton;

	@FXML
	private Button guardarButton;

	@FXML
	private TableColumn<Materia, Integer> horasPracticasTableColumn;

	@FXML
	private TextField horasPracticasTextField;

	@FXML
	private TableColumn<Materia, Integer> horasTeoricasTableColumn;

	@FXML
	private TextField horasTeoricasTextField;

	@FXML
	private Button limpiarButton;

	@FXML
	private TableView<Materia> materiasTableView;

	@FXML
	private TableColumn<Materia, String> nombreTableColumn;

	@FXML
	private TextField nombreTextField;

	@FXML
	private TableColumn<Materia, Integer> nrcTableColumn;

	@FXML
	private TextField nrcTextField;

	@FXML
	private TableColumn<Materia, String> profesorTableColumn;

	@FXML
	private TextField profesorTextField;

	private ObservableList<Materia> observerMaterias = FXCollections
			.observableArrayList();
	private Materia materiaSelected = null;
	private final ArrayList<Materia> materiasToInsert = new ArrayList<>();
	private final ArrayList<Materia> materiasToUpdate = new ArrayList<>();
	private final ArrayList<Materia> materiasToDelete = new ArrayList<>();

	private final IMateriaDAO materiaDAO = new MateriaDAO();

	@FXML
	void initialize() {
		nrcTableColumn.setCellValueFactory(new PropertyValueFactory<>("nrc"));
		nombreTableColumn.setCellValueFactory(
            new PropertyValueFactory<>("nombre")
		);
		creditosTableColumn.setCellValueFactory(
            new PropertyValueFactory<>("creditos")
		);
		horasTeoricasTableColumn.setCellValueFactory(
            new PropertyValueFactory<>("horasTeoricas")
		);
		horasPracticasTableColumn.setCellValueFactory(
            new PropertyValueFactory<>("horasPracticas")
		);
		profesorTableColumn.setCellValueFactory(
			new PropertyValueFactory<>("profesor")
		);
		materiasTableView.setItems(observerMaterias);

		agregarButton.setOnAction(agregarButtonHandler());
		editarButton.setOnAction(editarButtonHandler());
		eliminarButton.setOnAction(eliminarButtonHandler());
		limpiarButton.setOnAction(limpiarButtonHandler());
		guardarButton.setOnAction(guardarButtonHandler());
		guardarBDButton.setOnAction(guardarBDButtonHandler());
		cargarBDButton.setOnAction(cargarBDButtonHandler());

		cargarBDButton.fire();
		cargarBDButton.setVisible(false);
	}

	/**
	 * Permite agregar una nueva materia a la tabla de materias.
	 *
	 * @return el evento
	 */
	private EventHandler<ActionEvent> agregarButtonHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
                if (nrcTextField.getText().isEmpty() || 
                    nombreTextField.getText().isEmpty() ||
                    creditosTextField.getText().isEmpty() ||
                    horasTeoricasTextField.getText().isEmpty() ||
                    horasPracticasTextField.getText().isEmpty()
                ) {
					new Alert(
                        AlertType.WARNING,
                        "Debes completar todos los campos"
					).show();
					return;
				}
				for (Materia materia : observerMaterias) {
					if (String.valueOf(materia.getNrc()).equals(nrcTextField.getText())) {
						new Alert(
                            AlertType.WARNING,
                            "No puedes añadir más de una materia con el mismo NRC."
						).show();
						return;
					}
				}
				Materia nuevaMateria = new Materia(
                    0, Integer.parseInt(nrcTextField.getText()),
                    nombreTextField.getText(),
                    Integer.parseInt(creditosTextField.getText()),
                    Integer.parseInt(horasTeoricasTextField.getText()),
					Integer.parseInt(horasPracticasTextField.getText()),
					profesorTextField.getText()
				);
				observerMaterias.add(nuevaMateria);
				materiasToInsert.add(nuevaMateria);
				cleanMateriaForm();
			}
		};
	}

	/**
	 * Permite editar una materia seleccionado de la tabla de materias, asignando sus datos al
	 * formulario de materia.
	 *
	 * @return el evento
	 */
	private EventHandler<ActionEvent> editarButtonHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				materiaSelected = materiasTableView.getSelectionModel().getSelectedItem();
				if (materiaSelected == null) {
					Alert warningAlert = new Alert(
							AlertType.WARNING,
							"Debes seleccionar una materia primero."
					);
					warningAlert.show();
					return;
				}
				nrcTextField.setText(String.valueOf(materiaSelected.getNrc()));
				nombreTextField.setText(materiaSelected.getNombre());
				creditosTextField.setText(String.valueOf(materiaSelected.getCreditos()));
				horasTeoricasTextField.setText(String.valueOf(materiaSelected.getHorasTeoricas()));
				horasPracticasTextField.setText(String.valueOf(materiaSelected.getHorasPracticas()));
				profesorTextField.setText(materiaSelected.getProfesor());
				guardarButton.setDisable(false);
			}
		};
	}

	/**
	 * Permite eliminar una materia seleccionada de la tabla de materias.
	 *
	 * @return el evento
	 */
	private EventHandler<ActionEvent> eliminarButtonHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				materiaSelected = materiasTableView.getSelectionModel().getSelectedItem();
				if (materiaSelected == null) {
					Alert warningAlert = new Alert(
						AlertType.WARNING,
						"Debes seleccionar una materia primero."
					);
					warningAlert.show();
					return;
				}
				Alert deleteAlert = new Alert(
					AlertType.CONFIRMATION,
					"¿Estás seguro de que deseas eliminar este alumno?"
				);
				if (deleteAlert.showAndWait().get() == ButtonType.OK) {
					materiasToDelete.add(materiaSelected);
					materiasTableView.getItems().remove(materiaSelected);
				}
			}
		};
	}

	/**
	 * Limpia el formulario de la materia.
	 *
	 * @return el evento
	 */
	private EventHandler<ActionEvent> limpiarButtonHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				cleanMateriaForm();
			}
		};
	}

	/**
	 * Permite guardar los cambios del formulario de materia despues de haber seleccionado una
	 * materia a editar.
	 *
	 * @return el evento
	 */
	private EventHandler<ActionEvent> guardarButtonHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (materiaSelected == null) {
					Alert warningAlert = new Alert(
						AlertType.WARNING,
						"Debes seleccionar una materia primero."
					);
					warningAlert.show();
					return;
				}

				materiaSelected.setNrc(Integer.parseInt(nrcTextField.getText()));
				materiaSelected.setNombre(nombreTextField.getText());
				materiaSelected.setCreditos(Integer.parseInt(creditosTextField.getText()));
				materiaSelected.setHorasTeoricas(Integer.parseInt(horasTeoricasTextField.getText()));
				materiaSelected.setHorasPracticas(Integer.parseInt(horasPracticasTextField.getText()));
				materiaSelected.setProfesor(profesorTextField.getText());

				materiasToUpdate.add(materiaSelected);
				cleanMateriaForm();
				guardarButton.setDisable(true);

				materiasTableView.refresh();
			}
		};
	}

	/**
	 * Permite guardar los datos de la tabla de materias en la base de datos.
	 *
	 * @return el evento
	 */
	private EventHandler<ActionEvent> guardarBDButtonHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for (Materia materia : materiasToInsert) {
					materiaDAO.insertMateria(materia);
				}
				for (Materia materia : materiasToUpdate) {
					materiaDAO.updateMateria(materia);
				}
				for (Materia materia : materiasToDelete) {
					materiaDAO.deleteMateria(materia);
				}
				Alert saveAlert = new Alert(AlertType.INFORMATION, "Materias guardadas.");
				saveAlert.show();
				materiasToInsert.clear();
				materiasToUpdate.clear();
				materiasToDelete.clear();
			}
		};
	}

	/**
	 * Permite cargar los datos de los alumnos desde la base de datos.
	 *
	 * @return el evento
	 */
	private EventHandler<ActionEvent> cargarBDButtonHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				materiasTableView.getItems().clear();
				materiaDAO.loadMaterias();
				observerMaterias = materiaDAO.getMaterias();
				materiasTableView.setItems(observerMaterias);
				materiasToInsert.clear();
				materiasToUpdate.clear();
				materiasToDelete.clear();
			}
		};
	}

	/**
	 * Limpia el formulario de la materia.
	 */
	private void cleanMateriaForm() {
		nrcTextField.clear();
		nombreTextField.clear();
		creditosTextField.clear();
		horasTeoricasTextField.clear();
		horasPracticasTextField.clear();
		profesorTextField.clear();
		materiaSelected = null;
	}
}
