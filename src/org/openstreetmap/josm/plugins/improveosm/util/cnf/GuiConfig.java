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

import com.telenav.josm.common.cnf.BaseConfig;


/**
 * Holds commonly used GUI properties.
 *
 * @author Beata
 * @version $Revision$
 */
// TODO : refactor this class, some of the fields does not follows coding conventions
public final class GuiConfig extends BaseConfig {

    private static final String CONFIG_FILE = "improveosm_gui.properties";
    private static final GuiConfig INSTANCE = new GuiConfig();

    private final String pluginName;
    private final String pluginTxt;
    private final String dialogTitle;

    /* button texts */
    private final String btnOkLbl;
    private final String btnCancelLbl;
    private final String btnResetLbl;

    /* button tool-tips */
    private final String btnFilterTlt;
    private final String btnCommentTlt;
    private final String btnSolveTlt;
    private final String btnReopenTlt;
    private final String btnInvalidTlt;
    private final String btnLocationTlt;

    /* edit dialog texts */
    private final String dlgFilterTitle;
    private final String dlgCommentTitle;
    private final String dlgSolveTitle;
    private final String dlgReopenTitle;
    private final String dlgInvalidTitle;
    private final String menuSolveCommentTitle;
    private final String menuReopenCommentTitle;
    private final String menuInvalidCommentTitle;

    /* info panel title */
    private final String pnlInfoTitle;

    /* history panel title */
    private final String pnlHistoryTitle;

    /* preference panel text */
    private final String layerPreferenceLbl;
    private final String locationPreferenceLbl;
    private final String openStreetMapPrefLbl;
    private final String customPrefLbl;
    private final String copyLocationPrefLbl;

    /* commonly used texts */
    private final String lblCopy;
    private final String lblStatus;
    private final String lblConfidence;
    private final String lblNotAvailable;

    /* feedback panel title */
    private final String pnlFeedbackTitle;
    private final String pnlFeedbackTxt;

    /* warning texts */
    private final String warningTitle;
    private final String txtInvalidComment;
    private final String txtMissingUsername;
    private final String txtInvalidInteger;
    private final String txtDataLayerSettings;

    /* error texts */
    private final String errorTitle;
    private final String txtFeedbackUrlError;
    private final String errorSiteTitle;
    private final String txtSiteError;

    /* tips texts */
    private final String locationBtnTipLbl;
    private final String locationBtnTipTxt;

    private final String deleteMenuItemLbl;
    private final String deleteMenuItemTlt;

    private GuiConfig() {
        super(CONFIG_FILE);

        pluginName = readProperty("plugin.name");
        pluginTxt = readProperty("plugin.txt");
        dialogTitle = readProperty("dialog.title");

        btnOkLbl = readProperty("btn.ok.lbl");
        btnCancelLbl = readProperty("btn.cancel.lbl");
        btnResetLbl = readProperty("btn.reset.lbl");

        btnFilterTlt = readProperty("btn.filter.tlt");
        btnCommentTlt = readProperty("btn.comment.tlt");
        btnSolveTlt = readProperty("btn.solve.tlt");
        btnReopenTlt = readProperty("btn.reopen.tlt");
        btnInvalidTlt = readProperty("btn.invalid.tlt");
        btnLocationTlt = readProperty("btn.location.tlt");

        dlgFilterTitle = readProperty("dialog.filter.title");
        dlgCommentTitle = readProperty("edit.dialog.comment.title");
        dlgSolveTitle = readProperty("edit.dialog.solve.title");
        dlgReopenTitle = readProperty("edit.dialog.reopen.title");
        dlgInvalidTitle = readProperty("edit.dialog.invalid.title");
        menuSolveCommentTitle = readProperty("edit.menu.solve.comment.title");
        menuReopenCommentTitle = readProperty("edit.menu.reopen.comment.title");
        menuInvalidCommentTitle = readProperty("edit.menu.invalid.comment.title");

        pnlInfoTitle = readProperty("details.info.title");
        pnlHistoryTitle = readProperty("details.history.title");
        layerPreferenceLbl = readProperty("layerPreference.lbl");
        locationPreferenceLbl = readProperty("locationPreference.lbl");
        openStreetMapPrefLbl = readProperty("locationPref.openstreetmap.lbl");
        customPrefLbl = readProperty("locationPref.custom.lbl");
        copyLocationPrefLbl = readProperty("locationPref.copyLocation.lbl");

        lblCopy = readProperty("copy.lbl");
        lblStatus = readProperty("status.lbl");
        lblConfidence = readProperty("confidence.lbl");
        lblNotAvailable = readProperty("na.lbl");

        pnlFeedbackTitle = readProperty("feedback.title");
        pnlFeedbackTxt = readProperty("feedback.txt");

        warningTitle = readProperty("warning.title");
        txtInvalidComment = readProperty("warning.invalid.comment");
        txtMissingUsername = readProperty("warning.missing.username");
        txtInvalidInteger = readProperty("warning.invalid.integer");
        txtDataLayerSettings = readProperty("warning.datalayer.settings");
        errorTitle = readProperty("error.title");
        txtFeedbackUrlError = readProperty("error.feedback");

        locationBtnTipLbl = readProperty("location.tips.title");
        locationBtnTipTxt = readProperty("location.tips.message");

        errorSiteTitle = readProperty("locationPref.error.title");
        txtSiteError = readProperty("locationPref.error.text");

        deleteMenuItemLbl = readProperty("layer.menu.delete.lbl");
        deleteMenuItemTlt = readProperty("layer.menu.delete.tlt");
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

    public String getDialogTitle() {
        return dialogTitle;
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

    public String getBtnLocationTlt() {
        return btnLocationTlt;
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

    public String getMenuSolveCommentTitle() {
        return menuSolveCommentTitle;
    }

    public String getMenuReopenCommentTitle() {
        return menuReopenCommentTitle;
    }

    public String getMenuInvalidCommentTitle() {
        return menuInvalidCommentTitle;
    }

    public String getPnlInfoTitle() {
        return pnlInfoTitle;
    }

    public String getPnlHistoryTitle() {
        return pnlHistoryTitle;
    }

    public String getLayersPreferenceLbl() {
        return layerPreferenceLbl;
    }

    public String getLocationPreferenceLbl() {
        return locationPreferenceLbl;
    }

    public String getLocationPrefOpenStreetMap() {
        return openStreetMapPrefLbl;
    }

    public String getLocationPrefCustom() {
        return customPrefLbl;
    }

    public String getCopyLocationPrefLbl() {
        return copyLocationPrefLbl;
    }

    public String getLblCopy() {
        return lblCopy;
    }

    public String getLblStatus() {
        return lblStatus;
    }

    public String getLblConfidence() {
        return lblConfidence;
    }

    public String getLblNotAvailable() {
        return lblNotAvailable;
    }

    public String getPnlFeedbackTitle() {
        return pnlFeedbackTitle;
    }

    public String getPnlFeedbackTxt() {
        return pnlFeedbackTxt;
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

    public String getTxtDataLayerSettings() {
        return txtDataLayerSettings;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public String getTxtFeedbackUrlError() {
        return txtFeedbackUrlError;
    }

    public String getLocationBtnTipLbl() {
        return locationBtnTipLbl;
    }

    public String getLocationBtnTipTxt() {
        return locationBtnTipTxt;
    }

    public String getWrongSiteTitle() {
        return errorSiteTitle;
    }

    public String getWrongSiteText() {
        return txtSiteError;
    }

    public String getDeleteMenuItemLbl() {
        return deleteMenuItemLbl;
    }

    public String getDeleteMenuItemTlt() {
        return deleteMenuItemTlt;
    }

    public String getLayerPreferenceLbl() {
        return layerPreferenceLbl;
    }

    public String getOpenStreetMapPrefLbl() {
        return openStreetMapPrefLbl;
    }

    public String getCustomPrefLbl() {
        return customPrefLbl;
    }

    public String getErrorSiteTitle() {
        return errorSiteTitle;
    }

    public String getTxtSiteError() {
        return txtSiteError;
    }
}