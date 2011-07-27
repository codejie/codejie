function SetMainContext()
{
    var szFullLocation = new String(parent.location);
    var szPageName;
    var nPos;

    if(szFullLocation != null)
    {
        nPos = szFullLocation.lastIndexOf('?');
        if(nPos > 0)
        {
            szPageName = szFullLocation.substring(nPos+1, 512);
            if(szPageName.substring(0, 5) == "page=")
            {
                szPageName = szPageName.substring(5, 512);
                parent.main.location = szPageName;
            }
        }
    }
}

function ShowCurrentDate()
{
    var date = new Date();
    var dateText;

    // Format current date into string
    dateText = date.getDate() + "." + (date.getMonth()+1) + "." + date.getYear() + "  ";

    // Format current time into string
    if(date.getHours() < 10)
        dateText = dateText + "0";
    dateText = dateText + date.getHours() + ":"
    if(date.getMinutes() < 10)
        dateText = dateText + "0";
    dateText = dateText + date.getMinutes();

    document.write(dateText);
}

function ShowBrowserName()
{
    document.write(navigator.appName);
    document.write(' ');
}

function GoBack()
{
    if(document.referrer != null && document.referrer != '')
    {
        document.write('Zpìt na <A HREF="');
        document.write(document.referrer);
        document.write('" TARGET="_top">');
        document.write(document.referrer);
        document.writeln('</A>');
    }
}

function SetLanguage(szLanguage)
{
    if(parent.main != null)
    {
        var szFullName = new String(parent.main.location);
        var szPageName;                 // Plain page name, e.g. "main.html"
        var szSectName;                 // Section name, e.g. "mpq"
        var szLangName;                 // Language name, e.g. "cz"
        var szAbsPath;                  // Absolute path, e.g. "www.zezula.net"
        var nPos;                       // Position

        // Retrieve the plain page name
        nPos = szFullName.lastIndexOf('/');
        szPageName = szFullName.substring(nPos, 512);
        szFullName = szFullName.substring(0, nPos);

        // Retrieve the section name
        nPos = szFullName.lastIndexOf('/');
        szSectName = szFullName.substring(nPos, 64);
        szFullName = szFullName.substring(0, nPos);

        // Retrieve the current language name
        nPos = szFullName.lastIndexOf('/');
        szLangName = szFullName.substring(nPos+1, 64);
        szAbsPath = szFullName.substring(0, nPos);

        // Move the menu and the main window
        parent.menu.location = szAbsPath + '/' + szLanguage + '/menu.html';
        parent.main.location = szAbsPath + '/' + szLanguage + szSectName + szPageName;
    }
}

function SetPages(pages)
{
    if(parent.main != null)
        parent.main.location = '../../' + pages + '/cz/main.html';
    else
        document.location = '../../' + pages + '/cz/main.html';
}

function RunMailClient(user, domain, ctry)
{
    szMailClient = "mail" + "to" + ":" + user + "@" + domain + "." + ctry;
    document.location = szMailClient;
}

function RunMyMailClient()
{
    RunMailClient("ladik", "zezula", "net");
}
