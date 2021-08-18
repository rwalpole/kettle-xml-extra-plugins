package uk.gov.nationalarchives.pdi.step.xml;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

import java.util.List;

@Step(
        id = "CanonicalStep",
        name = "CanonicalStep.Name",
        description = "CanonicalStep.TooltipDesc",
        image = "CanonicalStep.svg",
        categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Transform",
        i18nPackageName = "uk.gov.nationalarchives.pdi.step.xml"
)
public class CanonicalStepMeta extends BaseStepMeta implements StepMetaInterface {

    private static final String ELEM_NAME_INPUT_FIELD = "inputField";
    private static final String ELEM_NAME_OUTPUT_FIELD = "outputField";

    private String inputField;
    private String outputField;

    /**
     * Constructor should call super() to make sure the base class has a chance to initialize properly.
     */
    public CanonicalStepMeta() {
        super();
    }

    /**
     * This method is called every time a new step is created and should allocate/set the step configuration
     * to sensible defaults. The values set here will be used by Spoon when a new step is created.
     */
    @Override
    public void setDefault() {
        setInputField("");
        setOutputField( "canonical_xml" );
    }

    public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name ) {
        return new CanonicalStepDialog( shell, meta, transMeta, name );
    }

    @Override
    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans) {
        return new CanonicalStep( stepMeta, stepDataInterface, copyNr, transMeta, trans );
    }

    @Override
    public StepDataInterface getStepData() {
        return new CanonicalStepData();
    }

    public String getOutputField() {
        return outputField;
    }

    @Override
    public boolean supportsErrorHandling() {
        return true;
    }

    public String getInputField() {
        return inputField;
    }

    /**
     * Setter for the name of the field added by this step
     * @param outputField the name of the field added
     */
    public void setOutputField( String outputField ) {
        this.outputField = outputField;
    }

    /**
     * Setter for the name of the input field to this step
     * @param inputField the name of the field used
     */
    public void setInputField( String inputField ) {
        this.inputField = inputField;
    }

    /**
     * This method is called by Spoon when a step needs to serialize its configuration to XML. The expected
     * return value is an XML fragment consisting of one or more XML tags.
     *
     * Please use org.pentaho.di.core.xml.XMLHandler to conveniently generate the XML.
     *
     * @return a string containing the XML serialization of this step
     */
    public String getXML() throws KettleValueException {
        StringBuilder xml = new StringBuilder();
        xml.append( XMLHandler.addTagValue(ELEM_NAME_INPUT_FIELD, inputField ));
        xml.append( XMLHandler.addTagValue(ELEM_NAME_OUTPUT_FIELD, outputField ));
        return xml.toString();
    }

    /**
     * This method is called by PDI when a step needs to load its configuration from XML.
     *
     * Please use org.pentaho.di.core.xml.XMLHandler to conveniently read from the
     * XML node passed in.
     *
     * @param stepnode  the XML node containing the configuration
     * @param databases  the databases available in the transformation
     * @param metaStore the metaStore to optionally read from
     */
    public void loadXML(Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
        try {
            setInputField( XMLHandler.getNodeValue( XMLHandler.getSubNode(stepnode, ELEM_NAME_INPUT_FIELD)));
            setOutputField( XMLHandler.getNodeValue( XMLHandler.getSubNode(stepnode, ELEM_NAME_OUTPUT_FIELD)));
        } catch ( Exception e ) {
            throw new KettleXMLException( "Demo plugin unable to read step info from XML node", e );
        }
    }

    /**
     * This method is called by Spoon when a step needs to serialize its configuration to a repository.
     * The repository implementation provides the necessary methods to save the step attributes.
     *
     * @param rep                 the repository to save to
     * @param metaStore           the metaStore to optionally write to
     * @param id_transformation   the id to use for the transformation when saving
     * @param id_step             the id to use for the step  when saving
     */
    public void saveRep(Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
            throws KettleException {
        try {
            rep.saveStepAttribute( id_transformation, id_step, ELEM_NAME_INPUT_FIELD, inputField );
            rep.saveStepAttribute( id_transformation, id_step, ELEM_NAME_OUTPUT_FIELD, outputField ); //$NON-NLS-1$
        } catch ( Exception e ) {
            throw new KettleException( "Unable to save step into repository: " + id_step, e );
        }
    }

    /**
     * This method is called by PDI when a step needs to read its configuration from a repository.
     * The repository implementation provides the necessary methods to read the step attributes.
     *
     * @param rep        the repository to read from
     * @param metaStore  the metaStore to optionally read from
     * @param id_step    the id of the step being read
     * @param databases  the databases available in the transformation
     */
    public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
            throws KettleException {
        try {
            inputField = rep.getStepAttributeString( id_step, ELEM_NAME_INPUT_FIELD); //$NON-NLS-1$
            outputField  = rep.getStepAttributeString( id_step, ELEM_NAME_OUTPUT_FIELD); //$NON-NLS-1$
        } catch ( Exception e ) {
            throw new KettleException( "Unable to load step from repository", e );
        }
    }

    /**
     * This method is called to determine the changes the step is making to the row-stream.
     * To that end a RowMetaInterface object is passed in, containing the row-stream structure as it is when entering
     * the step. This method must apply any changes the step makes to the row stream. Usually a step adds fields to the
     * row-stream.
     *
     * @param inputRowMeta    the row structure coming in to the step
     * @param name         the name of the step making the changes
     * @param info        row structures of any info steps coming in
     * @param nextStep      the description of a step this step is passing rows to
     * @param space        the variable space for resolving variables
     * @param repository    the repository instance optionally read from
     * @param metaStore      the metaStore to optionally read from
     */
    public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
                          VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {

        /*
         * This implementation appends the outputField to the row-stream
         */

        // a value meta object contains the meta data for a field
        ValueMetaInterface v = new ValueMetaString( outputField );

        // setting trim type to "both"
        v.setTrimType( ValueMetaInterface.TRIM_TYPE_BOTH );

        // the name of the step that adds this field
        v.setOrigin( name );

        // modify the row structure and add the field this step generates
        inputRowMeta.addValueMeta( v );
    }


}
