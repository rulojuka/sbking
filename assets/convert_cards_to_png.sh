#!/bin/bash

# This bash script covnerts the cards stored at assets/cards/ in .sgv format to
# a .png format, which is suitable for the Java.awt stack.
# Instructions: point your terminal at ./assets/ to run this script.
# Requirements: inkscape, bash terminal.

CARDS_SVG_PATH="./cards/"
INKSCAPE_EXPORT_FLAG=""

function set_inkscape_export_flag() {
	version=`inkscape --version | cut -f2 -d" " | cut -f1 -d"."`
	if [ $version -gt 0 ]
	then
		INKSCAPE_EXPORT_FLAG='--export-filename'
	else
		INKSCAPE_EXPORT_FLAG="--export-png"
	fi
}


function convert_svg_to_png() {
	echo converting "$1" to "$2"
	
	# Export using inkscape, on 125 dpi
	inkscape "$INKSCAPE_EXPORT_FLAG=$2" -d 125 "$1"
	echo " "
}

function convert_name() {
	declare -A name_convertion
	name_convertion=(
		["nicubunu-Card-backs-grid-blue.svg"]="back_blue.png"
		["nicubunu-Card-backs-grid-green.svg"]="back_green.png"
		["nicubunu-Card-backs-grid-red.svg"]="back_red.png"

		["nicubunu-White-deck-10-of-clubs.svg"]="ct.png"
		["nicubunu-White-deck-10-of-diamonds.svg"]="dt.png"
		["nicubunu-White-deck-10-of-hearts.svg"]="ht.png"
		["nicubunu-White-deck-10-of-spades.svg"]="st.png"
		["nicubunu-White-deck-2-of-clubs.svg"]="c2.png"
		["nicubunu-White-deck-2-of-diamonds.svg"]="d2.png"
		["nicubunu-White-deck-2-of-hearts.svg"]="h2.png"
		["nicubunu-White-deck-2-of-spades.svg"]="s2.png"
		["nicubunu-White-deck-3-of-clubs.svg"]="c3.png"
		["nicubunu-White-deck-3-of-diamonds.svg"]="d3.png"
		["nicubunu-White-deck-3-of-hearts.svg"]="h3.png"
		["nicubunu-White-deck-3-of-spades.svg"]="s3.png"
		["nicubunu-White-deck-4-of-clubs.svg"]="c4.png"
		["nicubunu-White-deck-4-of-diamonds.svg"]="d4.png"
		["nicubunu-White-deck-4-of-hearts.svg"]="h4.png"
		["nicubunu-White-deck-4-of-spades.svg"]="s4.png"
		["nicubunu-White-deck-5-of-clubs.svg"]="c5.png"
		["nicubunu-White-deck-5-of-diamonds.svg"]="d5.png"
		["nicubunu-White-deck-5-of-hearts.svg"]="h5.png"
		["nicubunu-White-deck-5-of-spades.svg"]="s5.png"
		["nicubunu-White-deck-6-of-clubs.svg"]="c6.png"
		["nicubunu-White-deck-6-of-diamonds.svg"]="d6.png"
		["nicubunu-White-deck-6-of-hearts.svg"]="h6.png"
		["nicubunu-White-deck-6-of-spades.svg"]="s6.png"
		["nicubunu-White-deck-7-of-clubs.svg"]="c7.png"
		["nicubunu-White-deck-7-of-diamonds.svg"]="d7.png"
		["nicubunu-White-deck-7-of-hearts.svg"]="h7.png"
		["nicubunu-White-deck-7-of-spades.svg"]="s7.png"
		["nicubunu-White-deck-8-of-clubs.svg"]="c8.png"
		["nicubunu-White-deck-8-of-diamonds.svg"]="d8.png"
		["nicubunu-White-deck-8-of-hearts.svg"]="h8.png"
		["nicubunu-White-deck-8-of-spades.svg"]="s8.png"
		["nicubunu-White-deck-9-of-clubs.svg"]="c9.png"
		["nicubunu-White-deck-9-of-diamonds.svg"]="d9.png"
		["nicubunu-White-deck-9-of-hearts.svg"]="h9.png"
		["nicubunu-White-deck-9-of-spades.svg"]="s9.png"
		["nicubunu-White-deck-Ace-of-clubs.svg"]="ca.png"
		["nicubunu-White-deck-Ace-of-diamonds.svg"]="da.png"
		["nicubunu-White-deck-Ace-of-hearts.svg"]="ha.png"
		["nicubunu-White-deck-Ace-of-spades.svg"]="sa.png"
		["nicubunu-White-deck-Jack-of-clubs.svg"]="cj.png"
		["nicubunu-White-deck-Jack-of-diamonds.svg"]="dj.png"
		["nicubunu-White-deck-Jack-of-hearts.svg"]="hj.png"
		["nicubunu-White-deck-Jack-of-spades.svg"]="sj.png"
		["nicubunu-White-deck-King-of-clubs.svg"]="ck.png"
		["nicubunu-White-deck-King-of-diamonds.svg"]="dk.png"
		["nicubunu-White-deck-King-of-hearts.svg"]="hk.png"
		["nicubunu-White-deck-King-of-spades.svg"]="sk.png"
		["nicubunu-White-deck-Queen-of-clubs.svg"]="cq.png"
		["nicubunu-White-deck-Queen-of-diamonds.svg"]="dq.png"
		["nicubunu-White-deck-Queen-of-hearts.svg"]="hq.png"
		["nicubunu-White-deck-Queen-of-spades.svg"]="sq.png"
	)
	
	key=${1/${CARDS_SVG_PATH}}
	value="${name_convertion[${key}]}"

	correct_name="${1/${key}/${value}}"
	correct_path=${correct_name/cards/exported}
	echo "${correct_path}"
}


function main() {
	set_inkscape_export_flag
	for filename in "${CARDS_SVG_PATH}"*.svg; do
		[ -e "$filename" ] || continue

		# Option -p creates directory if it doesn't exists.
		mkdir -p "./exported"
		
		correct_path=${filename/cards/exported}
		correct_ext=${correct_path/.svg/.png}
		new_name=$(convert_name "${filename}")
		
 		convert_svg_to_png "${filename}" "${new_name}"
	done
}

main
