/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013 Zimbra Software, LLC.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.4 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.cs.account.cache;

import java.util.List;

import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.cs.mime.MimeTypeInfo;

public interface IMimeTypeCache {
    public void flushCache(Provisioning prov) throws ServiceException;
    
    public List<MimeTypeInfo> getAllMimeTypes(Provisioning prov) throws ServiceException;
    
    public List<MimeTypeInfo> getMimeTypes(Provisioning prov, String mimeType) throws ServiceException;
}
