/*
 * LanguageClassifier.java
 *
 * Copyright (c) 2012  Francisco Gómez Carrasco
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Report bugs or new features to: flikxxi@gmail.com
 */
package com.softenido.cafecore.statistics.classifier;

import com.softenido.cafecore.gauge.GaugeProgress;
import com.softenido.cafecore.gauge.GaugeView;
import com.softenido.cafecore.logging.StatusNotifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author franci
 */
public interface LanguageClassifier
{
    boolean add(final String... languages);
    Score classify(String text);
    Score classify(InputStream text);
    void save(OutputStream out, int min, int max, String... allowedCategories) throws UnsupportedEncodingException;
    void saveGZ(OutputStream out, int min, int max, String... allowedCategories) throws UnsupportedEncodingException, IOException, NoSuchAlgorithmException;
    boolean firstPass();
    boolean secondPass();
    void setStatusNotifier(StatusNotifier statusNotifier);
}
