## Process this file with autoconf to create configure. -*- autoconf -*-
# Copyright 2001  Free Software Foundation, Inc.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
# 02110-1301  USA


dnl FIXME: Is this really new enough?
AC_PREREQ(2.50)


## ------------------------ ##
## Autoconf initialisation. ##
## ------------------------ ##
AC_INIT([libltdl], [1.2], [bug-libtool@gnu.org])
AC_CONFIG_SRCDIR([ltdl.c])


## ------------------------------- ##
## Libltdl specific configuration. ##
## ------------------------------- ##

AC_CONFIG_AUX_DIR([.])

if test -z "$enable_ltdl_install$enable_ltdl_convenience"; then
  if test -f ${srcdir}/ltmain.sh; then
    # if libltdl is libtoolized, it is assumed to be stand-alone and
    # installed unless the command line overrides it (tested above)
    enable_ltdl_install=yes
  else
    AC_MSG_WARN([*** The top-level configure must select either])
    AC_MSG_WARN([*** [A""C_LIBLTDL_INSTALLABLE] or [A""C_LIBLTDL_CONVENIENCE].])
    AC_MSG_ERROR([*** Maybe you want to --enable-ltdl-install?])
  fi
fi


## ------------------------ ##
## Automake Initialisation. ##
## ------------------------ ##
AM_INIT_AUTOMAKE(AC_PACKAGE_TARNAME, AC_PACKAGE_VERSION, -)
AM_CONFIG_HEADER([config.h:config-h.in])


## ------------------ ##
## C compiler checks. ##
## ------------------ ##
AC_PROG_CC
AC_C_CONST
AC_C_INLINE


## ----------------------- ##
## Libtool initialisation. ##
## ----------------------- ##
AC_LIBTOOL_WIN32_DLL
m4_defun([_LT_AC_LANG_CXX_CONFIG], [:])
m4_defun([_LT_AC_LANG_F77_CONFIG], [:])
AC_PROG_LIBTOOL
AC_SUBST([LIBTOOL_DEPS])

AC_LIB_LTDL


## -------- ##
## Outputs. ##
## -------- ##
AC_CONFIG_FILES([Makefile])
AC_OUTPUT
