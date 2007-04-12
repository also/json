package org.ry1.json;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;

class JsonReader {
	private enum Char {
		OBJECT_END, ARRAY_END, COLON, COMMA
	};

	private static HashMap<Character, Character> escapes = new HashMap<Character, Character>();
	static {
		escapes.put('"', '"');
		escapes.put('\\', '\\');
		escapes.put('/', '/');
		escapes.put('b', '\b');
		escapes.put('f', '\f');
		escapes.put('n', '\n');
		escapes.put('r', '\r');
		escapes.put('t', '\t');
	}

	private CharacterIterator it;

	private char currentChar;

	private Object token;

	private StringBuilder builder = new StringBuilder();

	private char next() {
		currentChar = it.next();
		return currentChar;
	}

	private void skipWhiteSpace() {
		while (Character.isWhitespace(currentChar)) {
			next();
		}
	}

	public JsonObject read(String string) {
		it = new StringCharacterIterator(string);
		currentChar = it.first();
		return (JsonObject) read();
	}
	
	private void require(char c) {
		if (currentChar != c) {
			throw new JsonFormatException();
		}
		next();
	}

	private Object read() {
		Object result = null;
		skipWhiteSpace();

		if (currentChar == '"') {
			next();
			result = string();
		}
		else if (currentChar == '[') {
			next();
			result = array();
		}
		else if (currentChar == ']') {
			result = Char.ARRAY_END;
		}
		else if (currentChar == ',') {
			result = Char.COMMA;
			next();
		}
		else if (currentChar == '{') {
			next();
			result = object();
		}
		else if (currentChar == '}') {
			result = Char.OBJECT_END;
			next();
		}
		else if (currentChar == ':') {
			result = Char.COLON;
			next();
		}
		else if (currentChar == 't' && next() == 'r' && next() == 'u' && next() == 'e') {
			result = Boolean.TRUE;
			next();
		}
		else if (currentChar == 'f' && next() == 'a' && next() == 'l' && next() == 's' && next() == 'e') {
			result = Boolean.FALSE;
			next();
		}
		else if (currentChar == 'n' && next() == 'u' && next() == 'l' && next() == 'l') {
			result = null;
			next();
		}
		else if (Character.isDigit(currentChar) || currentChar == '-') {
			result = number();
		}
		else {
			throw new JsonFormatException();
		}

		token = result;
		return result;
	}

	private JsonObject object() {
		JsonObject result = new JsonObject();
		skipWhiteSpace();
		require('"');
		String key = string();
		while (token != Char.OBJECT_END) {
			skipWhiteSpace();
			require(':');
			result.set((String) key, read());
			if (read() == Char.COMMA) {
				skipWhiteSpace();
				require('"');
				key = string();
			}
		}

		return result;
	}

	private JsonArray array() {
		JsonArray result = new JsonArray();
		Object value = read();
		while (token != Char.ARRAY_END) {
			result.addValue(value);
			if (read() == Char.COMMA) {
				value = read();
			}
		}
		return result;
	}

	private Number number() {
		boolean useDouble = false;
		builder.setLength(0);
		if (currentChar == '-') {
			add();
		}
		addDigits();
		if (currentChar == '.') {
			useDouble = true;
			add();
			addDigits();
		}
		if (currentChar == 'e' || currentChar == 'E') {
			useDouble = true;
			add();
			if (currentChar == '+' || currentChar == '-') {
				add();
			}
			addDigits();
		}
		
		if (useDouble) {
			return new Double(builder.toString());
		}
		else {
			return new Long(builder.toString());
		}
	}

	private String string() {
		builder.setLength(0);
		while (currentChar != '"') {
			if (currentChar == '\\') {
				next();
				if (currentChar == 'u') {
					add(unicode());
				}
				else {
					Character value = escapes.get(currentChar);
					if (value != null) {
						add(value.charValue());
					}
				}
			}
			else {
				add();
			}
		}
		next();

		return builder.toString();
	}

	private void add(char c) {
		builder.append(c);
		next();
	}

	private void add() {
		add(currentChar);
	}

	private void addDigits() {
		if (!Character.isDigit(currentChar)) {
			throw new JsonFormatException();
		}
		while (Character.isDigit(currentChar)) {
			add();
		}
	}

	private char unicode() {
		int value = 0;
		for (int i = 0; i < 4; ++i) {
			switch (next()) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				value = (value << 4) + currentChar - '0';
				break;
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
				value = (value << 4) + currentChar - 'k';
				break;
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
				value = (value << 4) + currentChar - 'K';
				break;
			default:
				throw new JsonFormatException();	
			}
		}
		return (char) value;
	}
}