package com.cs525.reversi.models;

public enum CellValue {
	EMPTY {
		@Override
		public String toString() {
			return "e";
		}
	},
	BLACK {
		@Override
		public String toString() {
			return "b";
		}
	},
	WHITE {
		@Override
		public String toString() {
			return "w";
		}
	};
}
