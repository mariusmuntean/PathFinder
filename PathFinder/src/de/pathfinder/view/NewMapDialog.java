package de.pathfinder.view;

import java.util.HashMap;
import java.util.function.Predicate;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

public class NewMapDialog extends TitleAreaDialog {
	private Text txtRows;
	private Text txtColumns;
	private Text txtStartCellRow;
	private Text txtStartCellColumn;
	private Text txtGoalCellRow;
	private Text txtGoalCellColumn;
	Button btnAddRandomObstacles, btnOK;
	
	HashMap<Widget, String> widgetToMessageMapping;
	HashMap<Widget, Boolean> widgetStateMapping;

	boolean hasValidRows, hasValidColumns, hasValidCellRow, hasValidCellCol;

	FocusListener focList;

	protected enum Dimension {
		ROW, COLUMN
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public NewMapDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setBlockOnOpen(true);

		focList = new FieldFocusListener();
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));
		new Label(container, SWT.NONE);

		Label lblRows = new Label(container, SWT.NONE);
		lblRows.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRows.setText("Rows");

		txtRows = new Text(container, SWT.BORDER);
		txtRows.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtRows.addVerifyListener(new WidthHeightVerifyListener());
		txtRows.addFocusListener(focList);

		Label lblColumns = new Label(container, SWT.NONE);
		lblColumns.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblColumns.setText("Columns");

		txtColumns = new Text(container, SWT.BORDER);
		txtColumns.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtColumns.addVerifyListener(new WidthHeightVerifyListener());
		txtColumns.addFocusListener(focList);

		Label lblStartCellCoordinates = new Label(container, SWT.NONE);
		lblStartCellCoordinates.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1,
				1));
		lblStartCellCoordinates.setText("Start Cell");

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(4, false));

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Row");

		txtStartCellRow = new Text(composite, SWT.BORDER);
		txtStartCellRow.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtStartCellRow.addFocusListener(focList);
		txtStartCellRow.addVerifyListener(new CellLocationVerifyListener(Dimension.ROW));

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Column");

		txtStartCellColumn = new Text(composite, SWT.BORDER);
		txtStartCellColumn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtStartCellColumn.addFocusListener(focList);
		txtStartCellColumn.addVerifyListener(new CellLocationVerifyListener(Dimension.COLUMN));

		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("Goal Cell");

		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_1.setLayout(new GridLayout(4, false));

		Label lblNewLabel_3 = new Label(composite_1, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("Row");

		txtGoalCellRow = new Text(composite_1, SWT.BORDER);
		txtGoalCellRow.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtGoalCellRow.addFocusListener(focList);
		txtGoalCellRow.addVerifyListener(new CellLocationVerifyListener(Dimension.ROW));

		Label lblNewLabel_4 = new Label(composite_1, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("Column");

		txtGoalCellColumn = new Text(composite_1, SWT.BORDER);
		txtGoalCellColumn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtGoalCellColumn.addFocusListener(focList);
		txtGoalCellColumn.addVerifyListener(new CellLocationVerifyListener(Dimension.COLUMN));

		Label lblRandomObstacles = new Label(container, SWT.NONE);
		lblRandomObstacles.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRandomObstacles.setText("Random Obstacles");

		btnAddRandomObstacles = new Button(container, SWT.CHECK);
		btnAddRandomObstacles.addFocusListener(focList);

		widgetToMessageMapping = new HashMap<Widget, String>() {
			{
				put(txtRows, "Input the number of rows (a.k.a. height) for the new map.");
				put(txtColumns, "Input the number of columns (a.k.a. width) for the new map.");
				put(txtStartCellRow, "Input the row number for the Starting Cell.");
				put(txtStartCellColumn, "Input the column number for the Starting Cell.");
				put(txtGoalCellRow, "Input the row number for the Goal Cell.");
				put(txtGoalCellColumn, "Input the column number for the Goal Cell.");
				put(btnAddRandomObstacles,
						"Check to add randomly-distributed obstacles to the map.");
			}
		};

		widgetStateMapping = new HashMap<Widget, Boolean>() {
			{
				put(txtRows, false);
				put(txtColumns, false);
				put(txtStartCellRow, false);
				put(txtStartCellColumn, false);
				put(txtGoalCellRow, false);
				put(txtGoalCellColumn, false);
			}
		};

		return container;
	}

	public int getRows() {
		int val = 0;
		try {
			val = Integer.valueOf(txtRows.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public int getColumns() {
		int val = 0;
		try {
			val = Integer.valueOf(txtColumns.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public int getStartCellRow() {
		int val = 0;
		try {
			val = Integer.valueOf(txtStartCellRow.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public int getStartCellColumn() {
		int val = 0;
		try {
			val = Integer.valueOf(txtStartCellColumn.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public int getGoalCellRow() {
		int val = 0;
		try {
			val = Integer.valueOf(txtGoalCellRow.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public int getGoalCellColumn() {
		int val = 0;
		try {
			val = Integer.valueOf(txtGoalCellColumn.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public boolean hasRandomObstacles() {
		return btnAddRandomObstacles.getSelection();
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		btnOK = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		btnOK.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 323);
	}

	private void updateState(Widget widget, boolean valid) {

		widgetStateMapping.put(widget, valid);

		boolean allValidationPassed = widgetStateMapping.values().stream()
				.filter(new Predicate<Boolean>() {
			@Override
			public boolean test(Boolean t) {
				return t == false;
			}
				}).count() == 0;

		btnOK.setEnabled(allValidationPassed);
	}

	private class WidthHeightVerifyListener implements VerifyListener {
		@Override
		public void verifyText(VerifyEvent e) {
			// If Backspace or Delete key is pressed forward it directly
			if (e.keyCode == 8 || e.keyCode == 127) {
				NewMapDialog.this.setErrorMessage(null);
				boolean valid = ((Text) e.widget).getText().length() > 1
						|| (e.character > '0' && e.character <= '9');
				updateState(e.widget, valid);
				return;
			}
			// Make sure only digits are entered
			String text = e.text;
			char[] chars = new char[text.length()];
			text.getChars(0, text.length(), chars, 0);
			for (int i = 0; i < chars.length; i++) {
				if (chars[i] < '0' || chars[i] > '9') {
					e.doit = false;
					NewMapDialog.this.setErrorMessage("Enter only digits between 0 and 9.");
					return;
				}
			}

			// Limit to at most 999 999 rows/columns
			if ((((Text) e.widget).getText().length() + 1 > 6)
					&& !(e.keyCode == 8 || e.keyCode == 127)) {
				e.doit = false;
				NewMapDialog.this.setErrorMessage("Enter at most 999999, i.e. 0.999999*10^6");
				return;
			}

			NewMapDialog.this.setErrorMessage(null);
			boolean valid = ((Text) e.widget).getText().length() > 0
					|| (e.character > '0' && e.character <= '9');
			updateState(e.widget, valid);
		}
	}

	private class CellLocationVerifyListener implements VerifyListener {

		Dimension dim;

		public CellLocationVerifyListener(Dimension dim) {
			this.dim = dim;
		}

		@Override
		public void verifyText(VerifyEvent e) {
			// If Backspace or Delete key is pressed forward it directly
			if (e.keyCode == 8 || e.keyCode == 127) {
				NewMapDialog.this.setErrorMessage(null);
				boolean valid = ((Text) e.widget).getText().length() > 1
						|| (e.character > '0' && e.character <= '9');
				updateState(e.widget, valid);
				return;
			}
			// Make sure only digits are entered
			String text = e.text;
			char[] chars = new char[text.length()];
			text.getChars(0, text.length(), chars, 0);
			for (int i = 0; i < chars.length; i++) {
				if (chars[i] < '0' || chars[i] > '9') {
					e.doit = false;
					NewMapDialog.this.setErrorMessage("Enter only digits between 0 and 9.");
					updateState(e.widget, false);
					return;
				}
			}

			// Limit value between 0 and the number of rows/columns
			if (((Text) e.widget).getText().length() == 0) {
				updateState(e.widget, true);
				return;
			}

			switch (this.dim) {
			case ROW: {
				int rows = NewMapDialog.this.getRows();
				int currentRow = Integer.valueOf(((Text) e.widget).getText() + e.text);
				if (currentRow >= rows) {
					e.doit = false;
					NewMapDialog.this.setErrorMessage(String.format(
							"Value must be between %d and %d", 0, rows));
					updateState(e.widget, false);
					return;
				}
				break;
			}
			case COLUMN: {
				int columns = NewMapDialog.this.getColumns();
				int currentCol = Integer.valueOf(((Text) e.widget).getText() + e.text);
				if (currentCol >= columns) {
					e.doit = false;
					NewMapDialog.this.setErrorMessage(String.format(
							"Value must be between %d and %d", 0, columns));
					updateState(e.widget, false);
					return;
				}
				break;
			}
			}

			NewMapDialog.this.setErrorMessage(null);
			boolean valid = ((Text) e.widget).getText().length() > 0
					|| (e.character > '0' && e.character <= '9');
			updateState(e.widget, valid);

		}
	}

	private class FieldFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			String msg = widgetToMessageMapping.get(e.widget);
			NewMapDialog.this.setMessage(msg, IMessageProvider.INFORMATION);
			NewMapDialog.this.setErrorMessage(null);
		}

		@Override
		public void focusLost(FocusEvent e) {
			NewMapDialog.this.setMessage(null);

		}
	}
}
