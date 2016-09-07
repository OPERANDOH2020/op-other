package eu.operando.api.model;
/*
 * Copyright (c) 2016 Oxford Computer Consultants Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT).
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *
 * Contributors:
 *    Matthew Gallagher (Oxford Computer Consultants) - Creation.
 * Initially developed in the context of OPERANDO EU project www.operando.eu
 */


import java.util.Vector;

/**
 * The email object which is used in the Email Services API.
 * 
 * It is used to make the JSON objects that are passed between modules;
 * it is okay that some fields are unused. 
 */
@SuppressWarnings("unused")
public class EmailNotification
{
	private Vector<String> to = new Vector<String>();  
	private Vector<String> cc = new Vector<String>();
	private Vector<String> bcc = new Vector<String>();
	private String content = "";
	private String subject = "";
	private Vector<Attachment> attachments = new Vector<Attachment>();
	
	public EmailNotification(Vector<String> to, Vector<String> cc, Vector<String> bcc, String content, String subject, Vector<Attachment> attachments)
	{
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.content = content;
		this.subject  = subject;
		this.attachments = attachments;
	}
}
