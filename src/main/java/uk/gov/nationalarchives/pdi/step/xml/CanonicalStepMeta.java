package uk.gov.nationalarchives.pdi.step.xml;

import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;

@Step(
        id = "CanonicalStep",
        name = "CanonicalStep.Name",
        description = "CanonicalStep.TooltipDesc",
        categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Transform",
        i18nPackageName = "uk.gov.nationalarchives.pdi.step.xml"
)
public class CanonicalStepMeta extends BaseStepMeta implements StepMetaInterface {

    private String outputField;

    /**
     * This method is called every time a new step is created and should allocate/set the step configuration
     * to sensible defaults. The values set here will be used by Spoon when a new step is created.
     */
    @Override
    public void setDefault() {
        setOutputField( "canonical_xml" );
    }

    @Override
    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans) {
        return null;
    }

    @Override
    public StepDataInterface getStepData() {
        return null;
    }

    public String getOutputField() {
        return outputField;
    }

    /**
     * Setter for the name of the field added by this step
     * @param outputField the name of the field added
     */
    public void setOutputField( String outputField ) {
        this.outputField = outputField;
    }



}
