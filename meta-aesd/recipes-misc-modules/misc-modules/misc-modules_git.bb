# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
LICENSE = "Unknown"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f098732a73b5f6f3430472f5b094ffdb"

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-asafg.git;protocol=ssh;branch=main \
           file://0001-Leave-only-misc-modules-and-scull-sub-directories.patch \
           file://sbin/module_load \
           file://sbin/module_unload \
           file://etc/init.d/misc-modules \
           "

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "369466085ce1e950c7f5fd308c26f8a6cc569b1f"

S = "${WORKDIR}/git"

inherit module

EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/misc-modules"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

FILES:${PN} += "${S}/../etc/init.d/misc-modules ${sysconfdir}/init.d/misc-modules"
FILES:${PN} += "${S}/../sbin/module_load ${sbindir}/module_load"
FILES:${PN} += "${S}/../sbin/module_unload ${sbindir}/module_unload"

inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "misc-modules"

# we need to use do_install:append () rather than just do_install (), 
# otherwise our do_install () overrides the one inheritted from class module
# and the modules are not being installed.
do_install:append () {
  install -d ${D}${sbindir}
	install -m 0755 ${S}/../sbin/module_load ${D}${sbindir}
	install -m 0755 ${S}/../sbin/module_unload ${D}${sbindir}
  install -d ${D}${sysconfdir}/init.d
  install -m 0755 ${S}/../etc/init.d/misc-modules ${D}${sysconfdir}/init.d
}
