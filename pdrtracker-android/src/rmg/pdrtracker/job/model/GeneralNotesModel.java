package rmg.pdrtracker.job.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: jenn
 * Date: 10/8/13
 * Time: 1:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeneralNotesModel implements Serializable {

    String generalNotes;

    public String getGeneralNotes() {
        return generalNotes;
    }

    public void setGeneralNotes(String generalNotes) {
        this.generalNotes = generalNotes;
    }
}
