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


/**
 * A wrapper class for email attachments.
 * 
 * It is used to make the JSON objects that are passed between modules;
 * it is okay that the fields are unused. 
 */
@SuppressWarnings("unused")
public class Attachment
{
	private String title = "";
	private String path = "";
}
