/*
 * Copyright (C) 2009 Kees Romijn
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.pentaho.pat.client.ui.widgets;

import com.google.gwt.user.client.ui.RichTextArea;

/**
 *
 *
 * @created Oct 28, 2009
 * @since 0.5.1
 * @author Kees Romijn
 */
public class MDXRichTextArea extends RichTextArea {

    String keyWordsStartsNewLine = "(case)|(from)|(select)|(member)|(set)|(where)"; //$NON-NLS-1$
    String keyWordStartsOnNewLine = "(select)|(where)|(with)|(from)|(non)|(member)"; //$NON-NLS-1$
    String keyWordsBlue = "(axis)|(as)|(columns?,?)|(from)|(member)|(on)|(rows?,?)|(select)|(where)|(with)"; //$NON-NLS-1$
    String KeyWordsFunction = "(AND)|(Abs)|(Acos)|(Acosh)|(AddCalculatedMembers)|(Aggregate)" + //$NON-NLS-1$
        "(AllMembers)|(Ancestor)|(Asc)|(AscB)|(AscW)|(Ascendants)|(Asin)|(Asinh)|(Atan2)|(Atanh)|(Atn)|(Avg)|" + //$NON-NLS-1$
        "(BottomCount)|(BottomPercent)|(BottomSum)|(CBool)|(CByte)|(CDate)|(CDbl)|(CInt)|(Cache)|(CalculatedChild)|" + //$NON-NLS-1$
        "(Caption)|(Cast)|(Children)|(Chr)|(ChrB)|(ChrW)|(ClosingPeriod)|(CoalesceEmpty)|(Correlation)|(Cos)|" + //$NON-NLS-1$
        "(Cosh)|(Count)|(Cousin)|(Covariance)|(CovarianceN)|(Crossjoin)|(CurrentDateMember)|(CurrentDateString)|" + //$NON-NLS-1$
        "(CurrentMember)|(DDB)|(DataMember)|(Date)|(DateAdd)|(DateDiff)|(DatePart)|(DateSerial)|(DateValue)|" + //$NON-NLS-1$
        "(Day)|(DefaultMember)|(Degrees)|(Descendants)|(Dimension)|(Dimensions)|(Distinct)|(DrilldownLevel)|" + //$NON-NLS-1$
        "(DrilldownLevelBottom)|(DrilldownLevelTop)|(DrilldownMember)|(Except)|(Exists)|(Exp)|(Extract)|(FV)|" + //$NON-NLS-1$
        "(Filter)|(FirstChild)|(FirstQ)|(FirstSibling)|(Fix)|(Format)|(FormatCurrency)|(FormatDateTime)|(FormatNumber)|" + //$NON-NLS-1$
        "(FormatPercent)|(Generate)|(Head)|(Hex)|(Hierarchize)|(Hierarchy)|(Hour)|(IIf)|(IPmt)|(IRR)|(IS)|(EMPTY)|" + //$NON-NLS-1$
        "(NULL)|(InStr)|(InStrRev)|(Int)|(Intersect)|(IsDate)|(IsEmpty)|(Item)|(LCase)|(LTrim)|(Lag)|(LastChild)|" + //$NON-NLS-1$
        "(LastPeriods)|(LastSibling)|(Lead)|(Left)|(Len)|(Level)|(Levels)|(LinRegIntercept)|(LinRegPoint)|(LinRegR2)|" + //$NON-NLS-1$
        "(LinRegSlope)|(LinRegVariance)|(Log)|(Log10)|(MIRR)|(Max)|(Median)|(Members)|(Mid)|(Min)|(Minute)|(Month)|" + //$NON-NLS-1$
        "(MonthName)|(Mtd)|(NON)|(NOT)|(NPV)|(NPer)|(Name)|(NextMember)|(NonEmptyCrossJoin)|(Now)|(Order)|(Ordinal)|(OR)|" + //$NON-NLS-1$
        "(Oct)|(OpeningPeriod)|(PPmt)|(PV)|(ParallelPeriod)|(ParamRef)|(Parameter)|(Parent)|(Percentile)|(PeriodsToDate)|" + //$NON-NLS-1$
        "(Pi)|(Pmt)|(Power)|(PrevMember)|(Properties)|(Qtd)|(RTrim)|(Radians)|(Rank)|(Rate)|(Replace)|(Right)|" + //$NON-NLS-1$
        "(Round)|(SLN)|(SYD)|(Second)|(SetToStr)|(Sgn)|(Siblings)|(Sin)|(Sinh)|(Space)|(Sqr)|(SqrtPi)|(Stddev)|" + //$NON-NLS-1$
        "(Stddev?P?)|(Stdev?P?)|(StdevP)|(Str)|(StrComp)|(StrReverse)|(StrToMember)|(StrToSet)|(StrToTuple)|(String)|" + //$NON-NLS-1$
        "(StripCalculatedMembers)|(Subset)|(Sum)|(Tail)|(Tan)|(Tanh)|(ThirdQ)|(Time)|(TimeSerial)|(TimeValue)|" + //$NON-NLS-1$
        "(Timer)|(ToggleDrillState)|(TopCount)|(TopPercent)|(TopSum)|(Trim)|(TupleToStr)|(TypeName)|(UCase)|" + //$NON-NLS-1$
        "(Union)|(UniqueName)|(Val)|(ValidMeasure)|(Value)|(Var)|(VarP)|(Variance)|(VarianceP)|(VisualTotals)|" + //$NON-NLS-1$
        "(Weekday)|(WeekdayName)|(Wtd)|(XOR)|(Year)|(Ytd)|(_CaseMatch)|(_CaseTest)"; //$NON-NLS-1$
    String startBlue = " <span style=\"color: blue;\">";//note starting space is important! //$NON-NLS-1$
    String startGreen = " <span style=\"color: green;\">"; //$NON-NLS-1$
    String endColor = " </span>"; //$NON-NLS-1$
    String startIndent = " <div style=\"margin-left: 10px;\">"; //$NON-NLS-1$
    String endIndent = " </div>"; //$NON-NLS-1$
    String afterThisKeywordNewLine = "(column?s? )"; //$NON-NLS-1$
    public MDXRichTextArea() {
        super();
    }

 /**
JAVADOC this will set the text of the MDXRichTextArea, and insert some fancy formatting.
*/

    @Override
    public void setText(String text) {
        super.setText(text);
        formatMDX();
    }

    private boolean nextBraceClose(int start, String[] pieces) {
        for (int i = start + 1; i < pieces.length; i++) {
            if (pieces[i].contains(")")) { //$NON-NLS-1$
                return true;
            }
            if (pieces[i].contains("(")) { //$NON-NLS-1$
                return false;
            }
        }
        return false;
    }

    private boolean nextBraceOpen(int start, String[] mdx) {
        for (int i = start + 1; i < mdx.length; i++) {
            if (mdx[i].contains("(")) { //$NON-NLS-1$
                return true;
            }
            if (mdx[i].contains(")")) { //$NON-NLS-1$
                return false;
            }
        }
        return false;
    }

    private String addSpaces(String s) {
        s = s.replaceAll("\\(", " ( "); //$NON-NLS-1$ //$NON-NLS-2$
        s = s.replaceAll("\\)", " ) "); //$NON-NLS-1$ //$NON-NLS-2$
        s = s.replaceAll("\\{", " { "); //$NON-NLS-1$ //$NON-NLS-2$
        s = s.replaceAll("\\}", " } "); //$NON-NLS-1$ //$NON-NLS-2$
        s = s.replaceAll("(\r\n)", " "); //$NON-NLS-1$ //$NON-NLS-2$
        s = s.replaceAll("[\r\n\t\f]", " "); //$NON-NLS-1$ //$NON-NLS-2$
        s = s.replaceAll(",", ", "); //$NON-NLS-1$ //$NON-NLS-2$
        return s;
    }

    void unFormatMdx() {
        this.setHTML(this.getHTML().replaceAll("<br>", " ")); //$NON-NLS-1$ //$NON-NLS-2$
        this.setHTML(this.getText());
    }

    void showHTML() {
        super.setText(this.getHTML().replaceAll("<br>", " ")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
JAVADOC Calling this method will reformat the mdx code
*/
public void formatMDX() {

        String MdxString = this.getText();

        // add spaces so make split on spaces possible
        MdxString = addSpaces(MdxString);

        String[] pieces = MdxString.split(" ");     // string[] cut into pieces //$NON-NLS-1$

        StringBuffer formattedMDXStringBuffer = new StringBuffer(); // this is where the formatted string goes
        boolean indent = false;
        String testString = null;
        for (int i = 0; i < pieces.length; i++) {
            testString = pieces[i].toLowerCase();
            if (testString.toLowerCase().matches(keyWordStartsOnNewLine.toLowerCase())) {
                if (indent) {
                    formattedMDXStringBuffer.append("<br> " + endIndent); //$NON-NLS-1$
                    indent = false;
                }
            }
            if (pieces[i].toLowerCase().matches(KeyWordsFunction.toLowerCase())) {
                pieces[i] = startGreen + pieces[i] + endColor;
            }
            if (pieces[i].toLowerCase().matches(keyWordsBlue.toLowerCase())) {
                pieces[i] = startBlue + pieces[i].toUpperCase() + endColor;
            }

            formattedMDXStringBuffer.append(pieces[i]);
            if (testString.matches(afterThisKeywordNewLine.toLowerCase())) {
                formattedMDXStringBuffer.append("<br>"); //$NON-NLS-1$
            }

            try {
                if (pieces.length > i) {
                    if (!pieces[i + 1].matches("[\\.\\]\\)\\[,]")) { //$NON-NLS-1$
                        formattedMDXStringBuffer.append(" "); //$NON-NLS-1$
                    }
                }
            }
            catch (Exception e) { // catch index out of bounds
            }
            // handle indent in nested functions
            if (pieces[i].contains("(")) { //$NON-NLS-1$
                if (nextBraceOpen(i, pieces)) {
                    formattedMDXStringBuffer.append("<br> " + startIndent); //$NON-NLS-1$
                }
            }
            if (pieces[i].contains(")")) { //$NON-NLS-1$
                if (nextBraceClose(i, pieces)) {
                    formattedMDXStringBuffer.append("<br> " + endIndent); //$NON-NLS-1$

                }
            }
            if (testString.matches(keyWordsStartsNewLine)) {
                formattedMDXStringBuffer.append("<br> " + startIndent); //$NON-NLS-1$
                indent = true;
            }
        } //endfor

        // put return after columns and rows keywords
        int place = formattedMDXStringBuffer.indexOf("COLUMNS," + endColor); //$NON-NLS-1$
        if (place > 0) {
            formattedMDXStringBuffer.insert(place + 8 + endColor.length(), "<br> "); //$NON-NLS-1$
        }
        place = formattedMDXStringBuffer.indexOf("ROWS," + endColor); //$NON-NLS-1$
        if (place > 0) {
            formattedMDXStringBuffer.insert(place + 5 + endColor.length(), "<br> "); //$NON-NLS-1$
        }
        String formatted = formattedMDXStringBuffer.toString();
        this.setEnabled(true);
        this.setHTML(formatted);
    }

}
