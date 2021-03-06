/*
 * Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program has been created in the hope that it will be useful.
 * It is distributed WITHOUT ANY WARRANTY of any Kind,
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * For more information, please contact SM2 Software & Services Management.
 * Mail: info@talaia-openppm.com
 * Web: http://www.talaia-openppm.com
 *
 * Module: core
 * File: DocumentationLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.dao.ContentFileDAO;
import es.sm2.openppm.core.dao.DocumentationDAO;
import es.sm2.openppm.core.model.impl.Contentfile;
import es.sm2.openppm.core.model.impl.Documentation;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Logic object for domain model class Documentation
 * @see es.sm2.openppm.logic.Documentation
 * @author Hibernate Generator by Javier Hernandez
 */
public final class DocumentationLogic extends AbstractGenericLogic<Documentation, Integer>{

    public void deleteDocument(int idDocumentation) throws Exception {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // Declare DAOs
            DocumentationDAO documentationDAO   = new DocumentationDAO(session);
            ContentFileDAO contentFileDAO       = new ContentFileDAO(session);

            // DAO find doc
            Documentation doc = documentationDAO.findById(idDocumentation);

            // DAO find content file
            Contentfile docFile = contentFileDAO.findByDocumentation(doc);

            // DAO delete doc
            documentationDAO.makeTransient(doc);

            // DAO delete file
            contentFileDAO.makeTransient(docFile);

            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
        finally {
            SessionFactoryUtil.getInstance().close();
        }
    }
}

