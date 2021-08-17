package uk.gov.nationalarchives.pdi.step.xml;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.core.widget.LabelText;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class CanonicalStepDialog extends BaseStepDialog implements StepDialogInterface {

    /**
     *  The PKG member is used when looking up internationalized strings.
     *  The properties file with localized keys is expected to reside in
     *  {the package of the class specified}/messages/messages_{locale}.properties
     */
    private static Class<?> PKG = CanonicalStepMeta.class; // for i18n purposes

    // this is the object the stores the step's settings
    // the dialog reads the settings from it when opening
    // the dialog writes the settings to it when confirmed
    private CanonicalStepMeta meta;

    // text field holding the name of the field to add to the row stream
    private LabelText xmlOutputFieldName;
    private Label wlField;
    private CCombo wField;

    /**
     * The constructor should simply invoke super() and save the incoming meta
     * object to a local variable, so it can conveniently read and write settings
     * from/to it.
     *
     * @param parent   the SWT shell to open the dialog in
     * @param in    the meta object holding the step's settings
     * @param transMeta  transformation description
     * @param sname    the step name
     */
    public CanonicalStepDialog(Shell parent, Object in, TransMeta transMeta, String sname ) {
        super( parent, (BaseStepMeta) in, transMeta, sname );
        meta = (CanonicalStepMeta) in;
    }

    /**
     * This method is called by Spoon when the user opens the settings dialog of the step.
     * It should open the dialog and return only once the dialog has been closed by the user.
     *
     * If the user confirms the dialog, the meta object (passed in the constructor) must
     * be updated to reflect the new step settings. The changed flag of the meta object must
     * reflect whether the step configuration was changed by the dialog.
     *
     * If the user cancels the dialog, the meta object must not be updated, and its changed flag
     * must remain unaltered.
     *
     * The open() method must return the name of the step after the user has confirmed the dialog,
     * or null if the user cancelled the dialog.
     */
    public String open() {
        // store some convenient SWT variables
        Shell parent = getParent();
        Display display = parent.getDisplay();

        // SWT code for preparing the dialog
        shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX );
        props.setLook( shell );
        setShellImage( shell, meta );

        // Save the value of the changed flag on the meta object. If the user cancels
        // the dialog, it will be restored to this saved value.
        // The "changed" variable is inherited from BaseStepDialog
        changed = meta.hasChanged();

        // The ModifyListener used on all controls. It will update the meta object to
        // indicate that changes are being made.
        ModifyListener lsMod = new ModifyListener() {
            public void modifyText( ModifyEvent e ) {
                meta.setChanged();
            }
        };

        // ------------------------------------------------------- //
        // SWT code for building the actual settings dialog        //
        // ------------------------------------------------------- //
        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;
        shell.setLayout( formLayout );
        shell.setText( BaseMessages.getString( PKG, "CanonicalStep.Shell.Title" ) );
        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        // Stepname line
        wlStepname = new Label( shell, SWT.RIGHT );
        wlStepname.setText( BaseMessages.getString( PKG, "System.Label.StepName" ) );
        props.setLook( wlStepname );
        fdlStepname = new FormData();
        fdlStepname.left = new FormAttachment( 0, 0 );
        fdlStepname.right = new FormAttachment( middle, -margin );
        fdlStepname.top = new FormAttachment( 0, margin );
        wlStepname.setLayoutData( fdlStepname );

        wStepname = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
        wStepname.setText( stepname );
        props.setLook( wStepname );
        wStepname.addModifyListener( lsMod );
        fdStepname = new FormData();
        fdStepname.left = new FormAttachment( middle, 0 );
        fdStepname.top = new FormAttachment( 0, margin );
        fdStepname.right = new FormAttachment( 100, 0 );
        wStepname.setLayoutData( fdStepname );

        // FieldName to evaluate
        wlField = new Label( shell, SWT.RIGHT );
        wlField.setText( BaseMessages.getString( PKG, "CanonicalStep.Input.Field.Label" ) );
        props.setLook( wlField );
        FormData fdlField = new FormData();
        fdlField.left = new FormAttachment( 0, 0 );
        fdlField.top = new FormAttachment( wStepname, 2 * margin );
        fdlField.right = new FormAttachment( middle, -margin );
        wlField.setLayoutData( fdlField );
        wField = new CCombo( shell, SWT.BORDER | SWT.READ_ONLY );
        wField.setEditable( true );
        props.setLook( wField );
        wField.addModifyListener( lsMod );

        FormData fdField = new FormData();
        fdField.left = new FormAttachment( middle, margin );
        fdField.top = new FormAttachment( wStepname, 2 * margin );
        fdField.right = new FormAttachment( 100, -margin );
        wField.setLayoutData( fdField );
        wField.addFocusListener( new FocusListener() {
            public void focusLost( org.eclipse.swt.events.FocusEvent e ) {
            }

            public void focusGained( org.eclipse.swt.events.FocusEvent e ) {
                Cursor busy = new Cursor( shell.getDisplay(), SWT.CURSOR_WAIT );
                shell.setCursor( busy );
                PopulateFields( wField );
                shell.setCursor( null );
                busy.dispose();
            }
        } );

        xmlOutputFieldName = new LabelText( shell, BaseMessages.getString( PKG, "CanonicalStep.Output.Field.Label" ), null );
        props.setLook(xmlOutputFieldName);
        xmlOutputFieldName.addModifyListener( lsMod );
        FormData fdValName = new FormData();
        fdValName.left = new FormAttachment( middle, 0 );
        fdValName.right = new FormAttachment( 100, 0 );
        fdValName.top = new FormAttachment( wField, margin );
        xmlOutputFieldName.setLayoutData( fdValName );




        // OK and cancel buttons
        wOK = new Button( shell, SWT.PUSH );
        wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
        wCancel = new Button( shell, SWT.PUSH );
        wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );
        setButtonPositions( new Button[] { wOK, wCancel }, margin, xmlOutputFieldName );

        // Add listeners for cancel and OK
        lsCancel = new Listener() {
            public void handleEvent( Event e ) {
                cancel();
            }
        };
        lsOK = new Listener() {
            public void handleEvent( Event e ) {
                ok();
            }
        };
        wCancel.addListener( SWT.Selection, lsCancel );
        wOK.addListener( SWT.Selection, lsOK );

        // default listener (for hitting "enter")
        lsDef = new SelectionAdapter() {
            public void widgetDefaultSelected( SelectionEvent e ) {
                ok();
            }
        };
        wStepname.addSelectionListener( lsDef );
        //wHelloFieldName.addSelectionListener( lsDef );

        // Detect X or ALT-F4 or something that kills this window and cancel the dialog properly
        shell.addShellListener( new ShellAdapter() {
            public void shellClosed( ShellEvent e ) {
                cancel();
            }
        } );

        // Set/Restore the dialog size based on last position on screen
        // The setSize() method is inherited from BaseStepDialog
        setSize();

        // populate the dialog with the values from the meta object
        populateDialog();

        // restore the changed flag to original value, as the modify listeners fire during dialog population
        meta.setChanged( changed );

        // open dialog and enter event loop
        shell.open();
        while ( !shell.isDisposed() ) {
            if ( !display.readAndDispatch() ) {
                display.sleep();
            }
        }

        // at this point the dialog has closed, so either ok() or cancel() have been executed
        // The "stepname" variable is inherited from BaseStepDialog
        return stepname;
    }

    /**
     * This helper method puts the step configuration stored in the meta object
     * and puts it into the dialog controls.
     */
    private void populateDialog() {
        wStepname.selectAll();
        xmlOutputFieldName.setText( meta.getOutputField() );
    }

    /**
     * Called when the user cancels the dialog.
     */
    private void cancel() {
        // The "stepname" variable will be the return value for the open() method.
        // Setting to null to indicate that dialog was cancelled.
        stepname = null;
        // Restoring original "changed" flag on the met aobject
        meta.setChanged( changed );
        // close the SWT dialog window
        dispose();
    }

    /**
     * Called when the user confirms the dialog
     */
    private void ok() {
        // The "stepname" variable will be the return value for the open() method.
        // Setting to step name from the dialog control
        stepname = wStepname.getText();
        // Setting the  settings to the meta object
        meta.setOutputField( xmlOutputFieldName.getText() );
        // close the SWT dialog window
        dispose();
    }

    private void PopulateFields( CCombo cc ) {
        if ( cc.isDisposed() ) {
            return;
        }
        try {
            String initValue = cc.getText();
            cc.removeAll();
            RowMetaInterface r = transMeta.getPrevStepFields( stepname );
            if ( r != null ) {
                cc.setItems( r.getFieldNames() );
            }
            if ( !Utils.isEmpty( initValue ) ) {
                cc.setText( initValue );
            }
        } catch ( KettleException ke ) {
            new ErrorDialog( shell, BaseMessages.getString( PKG, "XsltDialog.FailedToGetFields.DialogTitle" ), BaseMessages
                    .getString( PKG, "XsltDialog.FailedToGetFields.DialogMessage" ), ke );
        }

    }

}
