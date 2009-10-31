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

    String keyWordsStartsNewLine = "(case)|(from)|(select)|(member)|(set)|(where)";
    String keyWordStartsOnNewLine = "(select)|(where)|(with)|(from)|(non)|(member)";
    String keyWordsBlue = "(axis)|(as)|(columns?,?)|(from)|(member)|(on)|(rows?,?)|(select)|(where)|(with)";
    String KeyWordsFunction = "(AND)|(Abs)|(Acos)|(Acosh)|(AddCalculatedMembers)|(Aggregate)" +
        "(AllMembers)|(Ancestor)|(Asc)|(AscB)|(AscW)|(Ascendants)|(Asin)|(Asinh)|(Atan2)|(Atanh)|(Atn)|(Avg)|" +
        "(BottomCount)|(BottomPercent)|(BottomSum)|(CBool)|(CByte)|(CDate)|(CDbl)|(CInt)|(Cache)|(CalculatedChild)|" +
        "(Caption)|(Cast)|(Children)|(Chr)|(ChrB)|(ChrW)|(ClosingPeriod)|(CoalesceEmpty)|(Correlation)|(Cos)|" +
        "(Cosh)|(Count)|(Cousin)|(Covariance)|(CovarianceN)|(Crossjoin)|(CurrentDateMember)|(CurrentDateString)|" +
        "(CurrentMember)|(DDB)|(DataMember)|(Date)|(DateAdd)|(DateDiff)|(DatePart)|(DateSerial)|(DateValue)|" +
        "(Day)|(DefaultMember)|(Degrees)|(Descendants)|(Dimension)|(Dimensions)|(Distinct)|(DrilldownLevel)|" +
        "(DrilldownLevelBottom)|(DrilldownLevelTop)|(DrilldownMember)|(Except)|(Exists)|(Exp)|(Extract)|(FV)|" +
        "(Filter)|(FirstChild)|(FirstQ)|(FirstSibling)|(Fix)|(Format)|(FormatCurrency)|(FormatDateTime)|(FormatNumber)|" +
        "(FormatPercent)|(Generate)|(Head)|(Hex)|(Hierarchize)|(Hierarchy)|(Hour)|(IIf)|(IPmt)|(IRR)|(IS)|(EMPTY)|" +
        "(NULL)|(InStr)|(InStrRev)|(Int)|(Intersect)|(IsDate)|(IsEmpty)|(Item)|(LCase)|(LTrim)|(Lag)|(LastChild)|" +
        "(LastPeriods)|(LastSibling)|(Lead)|(Left)|(Len)|(Level)|(Levels)|(LinRegIntercept)|(LinRegPoint)|(LinRegR2)|" +
        "(LinRegSlope)|(LinRegVariance)|(Log)|(Log10)|(MIRR)|(Max)|(Median)|(Members)|(Mid)|(Min)|(Minute)|(Month)|" +
        "(MonthName)|(Mtd)|(NON)|(NOT)|(NPV)|(NPer)|(Name)|(NextMember)|(NonEmptyCrossJoin)|(Now)|(Order)|(Ordinal)|(OR)|" +
        "(Oct)|(OpeningPeriod)|(PPmt)|(PV)|(ParallelPeriod)|(ParamRef)|(Parameter)|(Parent)|(Percentile)|(PeriodsToDate)|" +
        "(Pi)|(Pmt)|(Power)|(PrevMember)|(Properties)|(Qtd)|(RTrim)|(Radians)|(Rank)|(Rate)|(Replace)|(Right)|" +
        "(Round)|(SLN)|(SYD)|(Second)|(SetToStr)|(Sgn)|(Siblings)|(Sin)|(Sinh)|(Space)|(Sqr)|(SqrtPi)|(Stddev)|" +
        "(Stddev?P?)|(Stdev?P?)|(StdevP)|(Str)|(StrComp)|(StrReverse)|(StrToMember)|(StrToSet)|(StrToTuple)|(String)|" +
        "(StripCalculatedMembers)|(Subset)|(Sum)|(Tail)|(Tan)|(Tanh)|(ThirdQ)|(Time)|(TimeSerial)|(TimeValue)|" +
        "(Timer)|(ToggleDrillState)|(TopCount)|(TopPercent)|(TopSum)|(Trim)|(TupleToStr)|(TypeName)|(UCase)|" +
        "(Union)|(UniqueName)|(Val)|(ValidMeasure)|(Value)|(Var)|(VarP)|(Variance)|(VarianceP)|(VisualTotals)|" +
        "(Weekday)|(WeekdayName)|(Wtd)|(XOR)|(Year)|(Ytd)|(_CaseMatch)|(_CaseTest)";
    String startBlue = " <span style=\"color: blue;\">";//note starting space is important!
    String startGreen = " <span style=\"color: green;\">";
    String endColor = " </span>";
    String startIndent = " <div style=\"margin-left: 10px;\">";
    String endIndent = " </div>";
    String afterThisKeywordNewLine = "(column?s? )";
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
            if (pieces[i].contains(")")) {
                return true;
            }
            if (pieces[i].contains("(")) {
                return false;
            }
        }
        return false;
    }

    private boolean nextBraceOpen(int start, String[] mdx) {
        for (int i = start + 1; i < mdx.length; i++) {
            if (mdx[i].contains("(")) {
                return true;
            }
            if (mdx[i].contains(")")) {
                return false;
            }
        }
        return false;
    }

    private String addSpaces(String s) {
        s = s.replaceAll("\\(", " ( ");
        s = s.replaceAll("\\)", " ) ");
        s = s.replaceAll("\\{", " { ");
        s = s.replaceAll("\\}", " } ");
        s = s.replaceAll("(\r\n)", " ");
        s = s.replaceAll("[\r\n\t\f]", " ");
        s = s.replaceAll(",", ", ");
        return s;
    }

    void unFormatMdx() {
        this.setHTML(this.getHTML().replaceAll("<br>", " "));
        this.setHTML(this.getText());
    }

    void showHTML() {
        super.setText(this.getHTML().replaceAll("<br>", " "));
    }

    /**
JAVADOC Calling this method will reformat the mdx code
*/
public void formatMDX() {

        String MdxString = this.getText();

        // add spaces so make split on spaces possible
        MdxString = addSpaces(MdxString);

        String[] pieces = MdxString.split(" ");     // string[] cut into pieces

        StringBuffer formattedMDXStringBuffer = new StringBuffer(); // this is where the formatted string goes
        boolean indent = false;
        String testString = null;
        for (int i = 0; i < pieces.length; i++) {
            testString = pieces[i].toLowerCase();
            if (testString.toLowerCase().matches(keyWordStartsOnNewLine.toLowerCase())) {
                if (indent) {
                    formattedMDXStringBuffer.append("<br> " + endIndent);
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
                formattedMDXStringBuffer.append("<br>");
            }

            try {
                if (pieces.length > i) {
                    if (!pieces[i + 1].matches("[\\.\\]\\)\\[,]")) {
                        formattedMDXStringBuffer.append(" ");
                    }
                }
            }
            catch (Exception e) { // catch index out of bounds
            }
            // handle indent in nested functions
            if (pieces[i].contains("(")) {
                if (nextBraceOpen(i, pieces)) {
                    formattedMDXStringBuffer.append("<br> " + startIndent);
                }
            }
            if (pieces[i].contains(")")) {
                if (nextBraceClose(i, pieces)) {
                    formattedMDXStringBuffer.append("<br> " + endIndent);

                }
            }
            if (testString.matches(keyWordsStartsNewLine)) {
                formattedMDXStringBuffer.append("<br> " + startIndent);
                indent = true;
            }
        } //endfor

        // put return after columns and rows keywords
        int place = formattedMDXStringBuffer.indexOf("COLUMNS," + endColor);
        if (place > 0) {
            formattedMDXStringBuffer.insert(place + 8 + endColor.length(), "<br> ");
        }
        place = formattedMDXStringBuffer.indexOf("ROWS," + endColor);
        if (place > 0) {
            formattedMDXStringBuffer.insert(place + 5 + endColor.length(), "<br> ");
        }
        String formatted = formattedMDXStringBuffer.toString();
        this.setEnabled(true);
        this.setHTML(formatted);
    }

}
