/*
 * ***** BEGIN LICENSE BLOCK *****
 * 
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013 VMware, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * 
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.day.singleday.tags;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.client.ZInvite.ZRole;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.TagItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogDeleteTag;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogRenameTag;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogTag;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.DialogConfirmDeleteAppointment;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.PageCalendar;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.Locators;

@SuppressWarnings("unused")
public class DeleteTagAppointment extends AjaxCommonTest {

	public DeleteTagAppointment() {
		logger.info("New "+ DeleteTagAppointment.class.getCanonicalName());

		// All tests start at the Calendar page
		super.startingPage = app.zPageCalendar;

		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -2913827779459595178L;
		{
		    put("zimbraPrefCalendarInitialView", "day");
		}};
	}

	@Bugs(ids = "75711")
	@Test(description = "Apply tag to appointment and delete same tag in day view",
			groups = { "functional" })
	public void DeleteTagAppointment_01() throws HarnessException {
		
		// Create objects
		String tz, apptSubject, apptBody, tag1, renameTag1, tagID;
		TagItem tag;
		tz = ZTimeZone.TimeZoneEST.getID();
		tag1 = ZimbraSeleniumProperties.getUniqueString();
		renameTag1 = ZimbraSeleniumProperties.getUniqueString();
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptBody = ZimbraSeleniumProperties.getUniqueString();
		
		// Create new appointment
		AppointmentItem appt = AppointmentItem.createAppointmentSingleDay(app.zGetActiveAccount(), Calendar.getInstance(), 120, null, apptSubject, apptBody, null, null);
        String apptId = appt.dApptID;

        // Create new tag and get tag ID
        app.zPageCalendar.zCreateTag(app, tag1, 8);
		tag = app.zPageCalendar.zGetTagItem(app.zGetActiveAccount(), tag1);
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		app.zGetActiveAccount().soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");;
		tagID = app.zGetActiveAccount().soapSelectValue("//mail:GetTagResponse//mail:tag[@name='"+ tag1 +"']", "id");
		
		// Apply tag to appointment
		app.zGetActiveAccount().soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>" + "<action id='" + apptId +"' op='tag' tn='"+ tag1 +"'/>" + "</ItemActionRequest>");
        
        // Delete the tag using the context menu
		DialogDeleteTag dialog = (DialogDeleteTag) app.zTreeCalendar.zTreeItem(
				Action.A_RIGHTCLICK, Button.B_DELETE, tag);
		dialog.zClickButton(Button.B_YES);
        app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
        
        // Verify appointment is not tagged
        app.zGetActiveAccount().soapSend("<GetAppointmentRequest xmlns='urn:zimbraMail' id='" + apptId + "'/>");
        ZAssert.assertEquals(app.zGetActiveAccount().soapSelectValue("//mail:appt", "t"), null, "Verify appointment is not tagged");
		
	}
	
	@Test(description = "Apply tag to appointment and delete tagged appointment in day view",
			groups = { "functional" })
	public void DeleteTagAppointment_2() throws HarnessException {
		
		// Create objects
		String tz, apptSubject, apptBody, tag1, renameTag1, tagID;
		TagItem tag;
		tz = ZTimeZone.TimeZoneEST.getID();
		tag1 = ZimbraSeleniumProperties.getUniqueString();
		renameTag1 = ZimbraSeleniumProperties.getUniqueString();
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptBody = ZimbraSeleniumProperties.getUniqueString();
		
		// Create new appointment
		AppointmentItem appt = AppointmentItem.createAppointmentSingleDay(app.zGetActiveAccount(), Calendar.getInstance(), 120, null, apptSubject, apptBody, null, null);
        String apptId = appt.dApptID;

        // Create new tag and get tag ID
        app.zPageCalendar.zCreateTag(app, tag1, 8);
		tag = app.zPageCalendar.zGetTagItem(app.zGetActiveAccount(), tag1);
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		app.zGetActiveAccount().soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");;
		tagID = app.zGetActiveAccount().soapSelectValue("//mail:GetTagResponse//mail:tag[@name='"+ tag1 +"']", "id");
		
		// Apply tag to appointment
		app.zGetActiveAccount().soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>" + "<action id='" + apptId +"' op='tag' tn='"+ tag1 +"'/>" + "</ItemActionRequest>");

		// Right click to appointment and delete it
        app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
        app.zPageCalendar.zListItem(Action.A_LEFTCLICK, apptSubject);
        DialogConfirmDeleteAppointment dlgConfirm = (DialogConfirmDeleteAppointment)app.zPageCalendar.zToolbarPressButton(Button.B_DELETE);
		dlgConfirm.zClickButton(Button.B_YES);
		SleepUtil.sleepMedium(); //testcase fails here due to timing issue so added sleep
		ZAssert.assertEquals(app.zPageCalendar.zIsAppointmentExists(apptSubject), false, "Verify appointment is deleted");
		
	}
}
