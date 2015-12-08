/*
 *  Copyright 2015 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.improveosm.util.cnf;

/**
 * Holds commonly used GUI properties.
 *
 * @author Beata
 * @version $Revision$
 */
public final class GuiConfig extends BaseConfig {

    private static final String CONFIG_FILE = "improveosm_gui.properties";
    private static final GuiConfig INSTANCE = new GuiConfig();

    private final String pluginName;
    private final String pluginTxt;

    /* button texts */
    private final String btnOkLbl;
    private final String btnCancelLbl;
    private final String btnResetLbl;

    private final String btnFilterTlt;
    private final String btnCommentTlt;
    private final String btnSolveTlt;
    private final String btnReopenTlt;
    private final String btnInvalidTlt;

    /* dialog titles */
    private final String dlgFilterTitle;
    private final String dlgCommentTitle;
    private final String dlgSolveTitle;
    private final String dlgReopenTitle;
    private final String dlgInvalidTitle;

    /* history panel title */
    private final String pnlHistoryTitle;

    private final String preferenceLbl;

    /* feedback panel title */
    private final String pnlFeedbackTitle;
    private final String pnlFeedbackTxt;
    /* error texts */
    private final String errorTitle;
    private final String txtFeedbackUrlError;

    /* warning texts */
    private final String warningTitle;
    private final String txtInvalidComment;
    private final String txtMissingUsername;
    private final String txtInvalidInteger;


    private GuiConfig() {
        super(CONFIG_FILE);

        pluginName = readProperty("plugin.name");
        pluginTxt = readProperty("plugin.txt");
        btnOkLbl = readProperty("btn.ok.lbl");
        btnCancelLbl = readProperty("btn.cancel.lbl");
        btnResetLbl = readProperty("btn.reset.lbl");
        btnFilterTlt = readProperty("btn.filter.tlt");
        btnCommentTlt = readProperty("btn.comment.tlt");
        btnSolveTlt = readProperty("btn.solve.tlt");
        btnReopenTlt = readProperty("btn.reopen.tlt");
        btnInvalidTlt = readProperty("btn.invalid.tlt");

        dlgFilterTitle = readProperty("dialog.filter.title");
        dlgCommentTitle = readProperty("edit.dialog.comment.title");
        dlgSolveTitle = readProperty("edit.dialog.solve.title");
        dlgReopenTitle = readProperty("edit.dialog.reopen.title");
        dlgInvalidTitle = readProperty("edit.dialog.invalid.title");

        pnlHistoryTitle = readProperty("details.history.title");
        preferenceLbl = readProperty("preference.lbl");
        pnlFeedbackTitle = readProperty("feedback.title");
        pnlFeedbackTxt = readProperty("feedback.txt");
        errorTitle = readProperty("error.title");
        txtFeedbackUrlError = readProperty("error.feedback");
        warningTitle = readProperty("warning.title");
        txtInvalidComment = readProperty("warning.invalid.comment");
        txtMissingUsername = readProperty("warning.missing.username");
        txtInvalidInteger = readProperty("warning.invalid.integer");
    }


    public static GuiConfig getInstance() {
        return INSTANCE;
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getPluginTxt() {
        return pluginTxt;
    }

    public String getBtnOkLbl() {
        return btnOkLbl;
    }

    public String getBtnCancelLbl() {
        return btnCancelLbl;
    }

    public String getBtnResetLbl() {
        return btnResetLbl;
    }

    public String getBtnFilterTlt() {
        return btnFilterTlt;
    }

    public String getBtnCommentTlt() {
        return btnCommentTlt;
    }

    public String getBtnSolveTlt() {
        return btnSolveTlt;
    }

    public String getBtnReopenTlt() {
        return btnReopenTlt;
    }

    public String getBtnInvalidTlt() {
        return btnInvalidTlt;
    }

    public String getDlgFilterTitle() {
        return dlgFilterTitle;
    }

    public String getDlgCommentTitle() {
        return dlgCommentTitle;
    }

    public String getDlgSolveTitle() {
        return dlgSolveTitle;
    }

    public String getDlgReopenTitle() {
        return dlgReopenTitle;
    }

    public String getDlgInvalidTitle() {
        return dlgInvalidTitle;
    }

    public String getPnlHistoryTitle() {
        return pnlHistoryTitle;
    }

    public String getPreferenceLbl() {
        return preferenceLbl;
    }

    public String getPnlFeedbackTitle() {
        return pnlFeedbackTitle;
    }

    public String getPnlFeedbackTxt() {
        return pnlFeedbackTxt;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public String getTxtFeedbackUrlError() {
        return txtFeedbackUrlError;
    }

    public String getWarningTitle() {
        return warningTitle;
    }

    public String getTxtInvalidComment() {
        return txtInvalidComment;
    }

    public String getTxtMissingUsername() {
        return txtMissingUsername;
    }

    public String getTxtInvalidInteger() {
        return txtInvalidInteger;
    }
}