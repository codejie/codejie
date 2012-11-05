<map version="freeplane 1.2.0">
<!--To view this file, download free mind mapping software Freeplane from http://freeplane.sourceforge.net -->
<node TEXT="Database Tables" ID="ID_1723255651" CREATED="1283093380553" MODIFIED="1351066129612"><hook NAME="MapStyle">
    <properties SHOW_NOTE_ICONS="true" show_icon_for_attributes="true" SHOW_ICON_FOR_ATTRIBUTES="true" SHOW_NOTES_IN_MAP="false"/>

<map_styles>
<stylenode LOCALIZED_TEXT="styles.root_node">
<stylenode LOCALIZED_TEXT="styles.predefined" POSITION="right">
<stylenode LOCALIZED_TEXT="default" COLOR="#000000" STYLE="as_parent" MAX_WIDTH="600"/>
<stylenode LOCALIZED_TEXT="defaultstyle.details"/>
<stylenode LOCALIZED_TEXT="defaultstyle.note"/>
<stylenode LOCALIZED_TEXT="defaultstyle.floating">
<cloud COLOR="#f0f0f0" SHAPE="ARC"/>
</stylenode>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.user-defined" POSITION="right">
<stylenode LOCALIZED_TEXT="styles.topic" COLOR="#18898b" STYLE="fork"/>
<stylenode LOCALIZED_TEXT="styles.subtopic" COLOR="#cc3300" STYLE="fork"/>
<stylenode LOCALIZED_TEXT="styles.subsubtopic" COLOR="#669900"/>
<stylenode LOCALIZED_TEXT="styles.important">
<icon BUILTIN="icon_not_found"/>
</stylenode>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.AutomaticLayout" POSITION="right">
<stylenode LOCALIZED_TEXT="AutomaticLayout.level.root" COLOR="#000000"/>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,1" COLOR="#0033ff"/>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,2" COLOR="#00b439"/>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,3" COLOR="#990000"/>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,4" COLOR="#111111"/>
</stylenode>
</stylenode>
</map_styles>
</hook>
<hook NAME="AutomaticEdgeColor" COUNTER="6"/>
<node TEXT="WordTable" POSITION="right" ID="ID_1991406085" CREATED="1350984011029" MODIFIED="1351064657351" VSHIFT="-50">
<edge COLOR="#ff0000" WIDTH="3"/>
<node TEXT="wordid" ID="ID_465252376" CREATED="1350984034643" MODIFIED="1350984040662"/>
<node TEXT="word" ID="ID_276174464" CREATED="1350984019639" MODIFIED="1350984028664"/>
<node TEXT="flag" ID="ID_969018033" CREATED="1351065569579" MODIFIED="1351065574725">
<node TEXT="1:dict" ID="ID_740083382" CREATED="1351065840516" MODIFIED="1351065850314"/>
<node TEXT="2: memory" ID="ID_697943359" CREATED="1351065851509" MODIFIED="1351065859042"/>
<node TEXT="4: dict &amp; memory" ID="ID_1501010520" CREATED="1351065860249" MODIFIED="1351065872217"/>
</node>
</node>
<node TEXT="SrcTable" POSITION="right" ID="ID_607019814" CREATED="1351064660831" MODIFIED="1351245213881" VSHIFT="-40">
<edge COLOR="#00ff00" WIDTH="3"/>
<node TEXT="srcid" ID="ID_1118126146" CREATED="1351064679521" MODIFIED="1351065600476"/>
<node TEXT="wordid" ID="ID_504427405" CREATED="1351065612593" MODIFIED="1351065616214"/>
<node TEXT="fmt" ID="ID_439567517" CREATED="1351065642351" MODIFIED="1351065818853">
<node TEXT="1: text" ID="ID_1211440503" CREATED="1351065887693" MODIFIED="1351065894450"/>
<node TEXT="2: html" ID="ID_805290053" CREATED="1351065895327" MODIFIED="1351065899360"/>
<node TEXT="3: xml" ID="ID_1781477498" CREATED="1351065904427" MODIFIED="1351677370785">
<attribute NAME="template" VALUE="&lt;x&gt;&lt;D&gt;dictid&lt;/D&gt; &lt;E&gt;E1&lt;/E&gt; &lt;E&gt;E2&lt;/E&gt; &lt;F&gt; &lt;S&gt;Symbol&lt;/S&gt; &lt;L&gt;Link&lt;/L&gt; &lt;I&gt; &lt;C&gt;category&lt;/C&gt; &lt;M&gt;Meaning&lt;/M&gt; &lt;/I&gt; &lt;I&gt; &lt;C&gt;category&lt;/C&gt; &lt;M&gt;Meaning&lt;/M&gt; &lt;/I&gt; &lt;/F&gt; &lt;F&gt; &lt;S&gt;Symbol&lt;/S&gt; &lt;L&gt;Link&lt;/L&gt; &lt;I&gt; &lt;C&gt;category&lt;/C&gt; &lt;M&gt;Meaning&lt;/M&gt; &lt;/I&gt; &lt;/F&gt;&lt;/X&gt;"/>
</node>
</node>
<node TEXT="orig" ID="ID_452731331" CREATED="1351065821519" MODIFIED="1351065837984">
<node TEXT="1:vicon" ID="ID_313569321" CREATED="1351065936346" MODIFIED="1351065942633"/>
<node TEXT="2: lingoshook" ID="ID_1626732890" CREATED="1351065943800" MODIFIED="1351065950558"/>
<node TEXT="3: input from xml" ID="ID_1475800425" CREATED="1351065951610" MODIFIED="1351065971681"/>
<node TEXT="4: input from client" ID="ID_1636905199" CREATED="1351065994214" MODIFIED="1351066005565"/>
</node>
<node TEXT="content" ID="ID_457246419" CREATED="1351066614578" MODIFIED="1351066619639"/>
</node>
<node TEXT="score" POSITION="right" ID="ID_652228933" CREATED="1346401472760" MODIFIED="1351066873204">
<edge COLOR="#ff00ff" WIDTH="3"/>
<font NAME="SansSerif" SIZE="12"/>
<hook NAME="FirstGroupNode"/>
<node TEXT="wordid" ID="ID_718003594" CREATED="1346401489484" MODIFIED="1350012037610">
<font NAME="SansSerif" SIZE="12"/>
</node>
<node TEXT="last" ID="ID_1646778938" CREATED="1346401655347" MODIFIED="1350012037610">
<font NAME="SansSerif" SIZE="12"/>
</node>
<node TEXT="next" ID="ID_1030407128" CREATED="1346401660185" MODIFIED="1350012037610">
<font NAME="SansSerif" SIZE="12"/>
</node>
<node TEXT="score" ID="ID_109601736" CREATED="1346401664369" MODIFIED="1350012037609">
<font NAME="SansSerif" SIZE="12"/>
</node>
</node>
<node TEXT="dict" POSITION="right" ID="ID_1954381151" CREATED="1346400607120" MODIFIED="1351666372593">
<edge COLOR="#00ffff" WIDTH="3"/>
<font NAME="SansSerif" SIZE="12"/>
<node TEXT="dictid" ID="ID_71748725" CREATED="1346400668581" MODIFIED="1350012037611">
<font NAME="SansSerif" SIZE="12"/>
</node>
<node TEXT="title" ID="ID_809035949" CREATED="1346400674613" MODIFIED="1350012037611">
<font NAME="SansSerif" SIZE="12"/>
</node>
</node>
<node TEXT="WordTable" POSITION="left" ID="ID_1249322990" CREATED="1351756576025" MODIFIED="1351756660881" VSHIFT="-30">
<edge COLOR="#ff0000" WIDTH="3"/>
<node TEXT="wordid" ID="ID_220867496" CREATED="1351756585928" MODIFIED="1351756592001"/>
<node TEXT="word" ID="ID_945788755" CREATED="1351756592908" MODIFIED="1351756596801"/>
<node TEXT="flag" ID="ID_612325976" CREATED="1351065569579" MODIFIED="1351065574725">
<node TEXT="1:dict" ID="ID_296823530" CREATED="1351065840516" MODIFIED="1351065850314"/>
<node TEXT="2: memory" ID="ID_105385456" CREATED="1351065851509" MODIFIED="1351065859042"/>
<node TEXT="4: dict &amp; memory" ID="ID_420690385" CREATED="1351065860249" MODIFIED="1351065872217"/>
</node>
<node TEXT="orig" ID="ID_71766758" CREATED="1351065821519" MODIFIED="1351065837984">
<node TEXT="1:vicon" ID="ID_1913973360" CREATED="1351065936346" MODIFIED="1351065942633"/>
<node TEXT="2: lingoshook" ID="ID_1924537156" CREATED="1351065943800" MODIFIED="1351065950558"/>
<node TEXT="3: input from xml" ID="ID_10896293" CREATED="1351065951610" MODIFIED="1351065971681"/>
<node TEXT="4: input from client" ID="ID_567141507" CREATED="1351065994214" MODIFIED="1351066005565"/>
</node>
<node TEXT="fmt" ID="ID_1984316827" CREATED="1351065642351" MODIFIED="1351065818853">
<node TEXT="1: text" ID="ID_838402064" CREATED="1351065887693" MODIFIED="1351065894450"/>
<node TEXT="2: html" ID="ID_1032709001" CREATED="1351065895327" MODIFIED="1351065899360"/>
<node TEXT="4: vicon" ID="ID_1015227714" CREATED="1351065904427" MODIFIED="1351849615651">
<attribute NAME="template" VALUE="&lt;x&gt;&lt;D&gt;dictid&lt;/D&gt; &lt;E&gt;E1&lt;/E&gt; &lt;E&gt;E2&lt;/E&gt; &lt;F&gt; &lt;S&gt;Symbol&lt;/S&gt; &lt;L&gt;Link&lt;/L&gt; &lt;I&gt; &lt;C&gt;category&lt;/C&gt; &lt;M&gt;Meaning&lt;/M&gt; &lt;/I&gt; &lt;I&gt; &lt;C&gt;category&lt;/C&gt; &lt;M&gt;Meaning&lt;/M&gt; &lt;/I&gt; &lt;/F&gt; &lt;F&gt; &lt;S&gt;Symbol&lt;/S&gt; &lt;L&gt;Link&lt;/L&gt; &lt;I&gt; &lt;C&gt;category&lt;/C&gt; &lt;M&gt;Meaning&lt;/M&gt; &lt;/I&gt; &lt;/F&gt;&lt;/X&gt;"/>
</node>
</node>
</node>
<node TEXT="ViconIndexTable" POSITION="left" ID="ID_1619734995" CREATED="1351849538468" MODIFIED="1351850209353">
<edge COLOR="#ff00ff" WIDTH="3"/>
<node TEXT="wordid" ID="ID_1690283239" CREATED="1351849558579" MODIFIED="1351849565145"/>
<node TEXT="start" ID="ID_1001886307" CREATED="1351849566427" MODIFIED="1351849571642"/>
<node TEXT="size" ID="ID_1472521629" CREATED="1351849572733" MODIFIED="1351849577222"/>
</node>
<node TEXT="SrcHtmlTable" POSITION="left" ID="ID_1184235182" CREATED="1351850439861" MODIFIED="1351850468295">
<edge COLOR="#00ffff" WIDTH="3"/>
</node>
<node TEXT="ViconDataFile" POSITION="left" ID="ID_1494901743" CREATED="1352099768577" MODIFIED="1352099778586">
<edge COLOR="#ffff00" WIDTH="3"/>
</node>
</node>
</map>
