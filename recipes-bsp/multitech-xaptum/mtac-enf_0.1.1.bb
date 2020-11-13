DESCRIPTION = "MTAC ENF (Xaptum ENF Access) Card"
HOMEPAGE = "http://www.xaptum.com"
SECTION = "base"
PRIORITY = "optional"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
DEPENDS = "virtual/kernel mtac mts-io"
RDEPENDS_${PN} = "kernel-module-mtac"
INC_PR = "r0"

SRCREV = "${PV}"

PR = "${INC_PR}.0-${MLINUX_KERNEL_VERSION}${MLINUX_KERNEL_EXTRA_VERSION}"

SRC_URI = " \
    git://github.com/xaptum/mtac-enf.git;protocol=git \
"
S = "${WORKDIR}/git"

inherit module

EXTRA_OEMAKE = " -C ${STAGING_KERNEL_DIR} \
                EXTRA_CFLAGS='-I${STAGING_INCDIR}/mts-kernel-headers' \
                KBUILD_VERBOSE=1 \
                M=${S} \
                modules \
                "

do_compile () {
    bbnote make   "$@"
    make   "$@"
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
    oe_runmake
}

PACKAGES = "kernel-module-${PN}"

FILES_kernel-module-${PN} = "${base_libdir}/modules/${KERNEL_VERSION}/extra/mtac_enf.ko"

PARALLEL_MAKE = ""

fakeroot do_install () {
    install -m 0755 -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra
    # use cp instead of install so the driver doesn't get stripped
    cp ${S}/mtac_enf.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra
}
