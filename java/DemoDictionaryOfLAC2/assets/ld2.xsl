<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes'/>

<xsl:template match="/">
	<HTML><HEAD></HEAD><BODY>
	<!-- Dictionary -->	
	<!-- Word -->	
	<!-- Result -->
	<xsl:apply-templates/>
	</BODY></HTML>
</xsl:template>

<xsl:template match="D">
</xsl:template>

<xsl:template match="W">
</xsl:template>

<xsl:template match="C">
	<xsl:for-each select="F">
		<DIV style="MARGIN: 5px 0px">
			<xsl:apply-templates/>
		</DIV>
	</xsl:for-each>
	
	<xsl:if test="E">
		Extension:
		<xsl:for-each select="E">
			<xsl:value-of select="."/>
		</xsl:for-each>		
	</xsl:if>
</xsl:template>

<xsl:template match="H">
	<SPAN style="LINE-HEIGHT: normal; COLOR: #000000; FONT-SIZE: 10.5pt">
		<xsl:apply-templates/>
	</SPAN>
</xsl:template>

<xsl:template match="L">
	<DIV style="MARGIN: 0px 0px 5px">
		<xsl:value-of select="."/>
	</DIV>
</xsl:template>

<xsl:template match="M">
	[<FONT COLOR="#009900">
		<xsl:value-of select="."/>
	</FONT>]
</xsl:template>

<xsl:template match="I">
	<DIV style="MARGIN: 0px 0px 5px">
		<xsl:apply-templates/>
	</DIV>
</xsl:template>

<xsl:template match="N">
	<DIV style="MARGIN: 4px 0px">
		<xsl:apply-templates/>
	</DIV>
</xsl:template>

<xsl:template match="U">
	<FONT color="#c00000">
		<xsl:value-of select="."/>
	</FONT>
</xsl:template>

<xsl:template match="x">
	<xsl:apply-templates/>
</xsl:template>

<xsl:template match="h">
	<I>
		<xsl:value-of select="."/>
	</I>
</xsl:template>

</xsl:stylesheet>
