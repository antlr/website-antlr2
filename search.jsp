<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="net.nutch.html.Entities" %>
<%@ page import="net.nutch.searcher.*" %>
<%@ page import="org.antlr.lime.support.*" %>
<%@ page import="org.antlr.lime.service.*" %>
<%@ page import="org.antlr.stringtemplate.*" %>

<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0

String message = request.getParameter("msg");
System.out.println("got error msg = "+message);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
	<title>Search Results</title>
</head>

<body link="#336699" vlink="#6FA2D4" text="#000000">

<div align="center">
<!-- BEGIN NAVIGATION -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
<td align="right">
<font size="2" face="arial,helvetica"><strong><a href="/">Home</a></strong> |
<strong><a href="/download/list">Download</a></strong> |
<strong><a href="/news/list">News</a></strong> |
<strong><a href="/about.html">About ANTLR</a></strong> |
<strong><a href="/support.html">Support</a></strong>
</font><br>
<img src="/images/shim.gif" width="780" height="5" alt="" border="0"><br>
</td>
</tr>
</table>
<!-- END NAVIGATION -->

<div align="center"><img src="/images/shim.gif" width="780" height="6" alt="" border="0"></div>

<!-- BEGIN BANNER -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
<td align="left" width="499">
<img src="/images/logo-head.gif" width="499" height="48" alt="" border="0">
</td>
<td align="left" width="100%" background="/images/head-shim.gif" bgcolor="#336699"><img src="/images/logo-head.gif" width="1" height="1" alt="" border="0"></td>

<td align="right" valign="top" width="150" background="/images/head-shim.gif" bgcolor="#336699"><img src="/images/shim.gif" width="150" height="8" alt="" border="0"><br>
<font size="2" face="arial,helvetica"><strong>Latest version is <font color="#ffffff">2.7.1.</font><br>
Download now! &raquo;</strong></font><br></td>


<td align="right" valign="middle" width="128" background="/images/head-shim.gif" bgcolor="#336699">
<a href="http://replace"><img src="/images/button-download.gif" width="122" height="29" alt="Download" align="right" border="0"></a></td>
</tr>
</table>
<!-- END BANNER -->

<div align="center"><img src="/images/shim.gif" width="780" height="1" alt="" border="0"></div>

<table width="100%" cellpadding="1" cellspacing="0" border="0">
<tr>
<td bgcolor="#ffffff" width="175" valign="top">
<!-- left column -->

<!-- BEGIN LEFT NAV -->
<table width="100%" cellpadding="3" cellspacing="1" border="0">
	<tr>
	<td bgcolor="#CBFF7D">
	<font size="2" face="arial,helvetica">
	<b><font color="#CBFF7D">&raquo;</font> <a href="/">Home</a></b><br>
	</font>
	</td>
	</tr>
	</table>
	<table width="100%" cellpadding="3" cellspacing="1" border="0">
	<tr>
	<td bgcolor="#CBFF7D">
	<font size="2" face="arial,helvetica">
	<b><font color="#CBFF7D">&raquo;</font> <a href="/download.html">Download</a></b><br>
	</font>
	</td>
	</tr>
	</table>
    <table width="100%" cellpadding="3" cellspacing="1" border="0">
	<tr>
	<td bgcolor="#CBFF7D">
	<font size="2" face="arial,helvetica">
	<b><font color="#CBFF7D">&raquo;</font> <a href="/news/list">News</a></b><br>
	</font>
	</td>
	</tr>
	</table>
	<table width="100%" cellpadding="3" cellspacing="1" border="0">
	<tr>
	<td colspan="2" bgcolor="#CBFF7D">
	<font size="2" face="arial,helvetica">
	<b><font color="#CBFF7D">&raquo;</font>Using ANTLR</b><br>
	</font>
	</td>
	</tr>
	
	<tr>
	<td bgcolor="#ffffff"></td>
	<td bgcolor="#ffffff">
	<font size="2" face="arial,helvetica">
	<b><font color="#ffffff">&raquo;</font> <a href="/doc/index.html">Documentation</a></b><br>
	</font>
	</td>
	</tr>
	
	<tr>
	<td bgcolor="#ffffff"></td>
	<td bgcolor="#ffffff">
	<font size="2" face="arial,helvetica">
	<b><font color="#ffffff">&raquo;</font> <a href="http://www.jguru.com/faq/ANTLR">FAQ</a></b><br>
	</font>
	</td>
	</tr>
	
	<tr>
	<td bgcolor="#ffffff"></td>
	<td bgcolor="#ffffff">
	<font size="2" face="arial,helvetica">
	<b><font color="#ffffff">&raquo;</font> <a href="/article/list">Articles</a></b><br>
	</font>
	</td>
	</tr>
	
	<tr>
	<td bgcolor="#ffffff"></td>
	<td bgcolor="#ffffff">
	<font size="2" face="arial,helvetica">
	<b><font color="#ffffff">&raquo;</font> <a href="/grammar/list">Grammars</a></b><br>
	</font>
	</td>
	</tr>
	
	<tr>
	<td bgcolor="#ffffff"></td>
	<td bgcolor="#ffffff">
	<font size="2" face="arial,helvetica">
	<b><font color="#ffffff">&raquo;</font> <a href="/share/list">File Sharing</a></b><br>
	</font>
	</td>
	</tr>
	
	<tr>
	<td bgcolor="#ffffff"></td>
	<td bgcolor="#ffffff">
	<font size="2" face="arial,helvetica">
	<b><font color="#ffffff">&raquo;</font> <a href="/javadoc/overview-summary.html">Code API</a></b><br>
	</font>
	</td>
	</tr>
	
	<tr>
	<td bgcolor="#ffffff"></td>
	<td bgcolor="#ffffff">
	<font size="2" face="arial,helvetica">
	<b><font color="#ffffff">&raquo;</font> <a href="/support.html">Tech Support</a></b><br>
	</font>
	</td>
	</tr>
	
	</table>
	
	
<table width="100%" cellpadding="3" cellspacing="1" border="0">
	<tr>
	<td colspan="2" bgcolor="#CBFF7D">
	<font size="2" face="arial,helvetica">
	<b><font color="#CBFF7D">&raquo;</font>About ANTLR</b><br>
	</font>
	</td>
	</tr>
	
	<tr>
	<td bgcolor="#ffffff"></td>
	<td bgcolor="#ffffff">
	<font size="2" face="arial,helvetica">
	<b><font color="#ffffff">&raquo;</font> <a href="/about.html">What is ANTLR</a></b><br>
	</font>
	</td>
	</tr>
	
	<tr>
	<td bgcolor="#ffffff"></td>
	<td bgcolor="#ffffff">
	<font size="2" face="arial,helvetica">
	<b><font color="#ffffff">&raquo;</font> <a href="/why.html">Why use ANTLR</a></b><br>
	</font>
	</td>
	</tr>
	
	<tr>
	<td bgcolor="#ffffff"></td>
	<td bgcolor="#ffffff">
	<font size="2" face="arial,helvetica">
	<b><font color="#ffffff">&raquo;</font> <a href="/showcase/list">Showcase</a></b><br>
	</font>
	</td>
	</tr>
	
	<tr>
	<td bgcolor="#ffffff"></td>
	<td bgcolor="#ffffff">
	<font size="2" face="arial,helvetica">
	<b><font color="#ffffff">&raquo;</font> <a href="/testimonial/list">Testimonials</a></b><br>
	</font>
	</td>
	</tr>
	
	<tr>
	<td bgcolor="#ffffff"></td>
	<td bgcolor="#ffffff">
	<font size="2" face="arial,helvetica">
	<b><font color="#ffffff">&raquo;</font> <a href="/doc/getting-started.html">Getting Started</a></b><br>
	</font>
	</td>
	</tr>
	
	<tr>
	<td bgcolor="#ffffff"></td>
	<td bgcolor="#ffffff">
	<font size="2" face="arial,helvetica">
	<b><font color="#ffffff">&raquo;</font> <a href="/license.html">Software License</a></b><br>
	</font>
	</td>
	</tr>
	<tr>
	<td bgcolor="#ffffff"></td>
	<td bgcolor="#ffffff">
	<font size="2" face="arial,helvetica">
	<b><font color="#ffffff">&raquo;</font> <a href="/blog/index.tml">ANTLR WebLogs</a></b><br>
	</font>
	</td>
	</tr>

	</table>

	<table width="100%" cellpadding="3" cellspacing="1" border="0">
	<tr>
	<td bgcolor="#CBFF7D">
	<font size="2" face="arial,helvetica">
	<b><font color="#CBFF7D">&raquo;</font><a href="/stringtemplate/index.tml">StringTemplate</a></b><br>
	</font>
	</td>
	</tr>
	</table>

	<table width="100%" cellpadding="3" cellspacing="1" border="0">
	<tr>
	<td bgcolor="#CBFF7D">
	<font size="2" face="arial,helvetica">
	<b><font color="#CBFF7D">&raquo;</font><a href="/pccts133.html">PCCTS</a></b><br>
	</font>
	</td>
	</tr>
	</table>

<br>
<br>

<img src="/images/shim.gif" width="173" height="1" alt="" border="0">

<!-- END LEFT NAV -->

</td>
<td bgcolor="#ffffff" width="100%" valign="top">
<!-- main column -->

<font face="arial,verdana,sanserif" size="3">
<b>Search Results</b><br><br>
</font>
<font face="arial,verdana,sanserif" size="2">

<%
	NutchBean bean = new NutchBean(new File("/var/data/nutch/crawl.db"));
	int start = 0;              // first hit to display
	System.out.println("SearchPage: doSearch(): start = " + start);

	int hitsPerPage = 10;      // number of hits to display
	System.out.println("SearchPage: doSearch(): hitsPerPage = " + hitsPerPage);
	int hitsPerSite = 0;       // max hits per site
	System.out.println("SearchPage: doSearch(): hitsPerSite = " + hitsPerSite);

	String queryString = request.getParameter("query");
	Query query = Query.parse(queryString);
	// perform query
	Hits hits = bean.search(query, start + hitsPerPage, hitsPerSite);
	int end = (int)Math.min(hits.getLength(), start + hitsPerPage);
	int length = end-start;
	Hit[] show = hits.getHits(start, length);
	HitDetails[] details = bean.getDetails(show); 
	String[] summaries = bean.getSummary(details, query);

	System.out.println("SearchPage: doSearch(): total hits = " + hits.getTotal());              System.out.println("SearchPage: doSearch(): total hits length= " + hits.getLength());            System.out.println("SearchPage: doSearch(): about to create new result vector");
	StringTemplateGroup templates = LimeApplication.stringTemplatesLib;
	StringTemplate resultsST = templates.getInstanceOf("search/results");
	//Vector resultList = new Vector();
	for (int i = 0; i < length; i++) {           // display the hits
	    Hit hit = show[i];
	    HitDetails detail = details[i];
	    String summary = summaries[i];
	    String title = detail.getValue("title");
	    String url = detail.getValue("url");                String id = "idx=" + hit.getIndexNo() + "&id=" + hit.getIndexDocNo();           
	    // int score = formatScore(hit.getScore());
	    float score = hit.getScore();
	    if (title == null || title.equals(""))
		    title = url;

	    SearchResult result =
			new SearchResult(hit, detail, title, url, summary, id, score);
	    //resultList.addElement(result);
		resultsST.setAttribute("results", result);
	}
/*
	if (hits.getTotal()!=0) {
	    System.out.println("Results " + (start+1) + " - " +
		(resultList.size()+start) + " (Total " + hits.getTotal() + ")");
	}
*/
%>

<%= resultsST.toString() %>

</font>

</td>
</tr>
</table>

</div>

</body>
</html>
