package it.polimi.ingsw.lim.utils;

import java.util.HashMap;



/**
 * This class translates text into morse and vice versa. Some changes were made to fit in the code.
 * It's only purpose is to create a joke CLI.
 * Credits: https://www.reddit.com/r/learnprogramming/comments/2e6ia1/java_how_to_use_a_hashmap_to_replace_characters/
 */
public class MorseParser {
    public static final java.util.Map<Character, String> codeLibrary;
    static{
        codeLibrary = new HashMap<>();
        codeLibrary.put('a',".-");              codeLibrary.put('b',"-...");
        codeLibrary.put('c',"-.-.");    codeLibrary.put('d',"-..");
        codeLibrary.put('e',".");               codeLibrary.put('f',"..-.");
        codeLibrary.put('g',"--.");             codeLibrary.put('h',"....");
        codeLibrary.put('i',"..");              codeLibrary.put('j',".---");
        codeLibrary.put('k',"-.-");             codeLibrary.put('l',".-..");
        codeLibrary.put('m',"--");              codeLibrary.put('n',"-.");
        codeLibrary.put('o',"---");             codeLibrary.put('p',".--.");
        codeLibrary.put('q',"--.-");    codeLibrary.put('r',".-.");
        codeLibrary.put('s',"...");             codeLibrary.put('t',"-");
        codeLibrary.put('u',"..-");             codeLibrary.put('v',"...-");
        codeLibrary.put('w',".--");             codeLibrary.put('x',"-..-");
        codeLibrary.put('y',"-.--");    codeLibrary.put('z',"--..");
        codeLibrary.put('1',".----");   codeLibrary.put('2',"..---");
        codeLibrary.put('3',"...--");   codeLibrary.put('4',".----");
        codeLibrary.put('5',".....");   codeLibrary.put('6',"-....");
        codeLibrary.put('7',"--...");   codeLibrary.put('8',"---..");
        codeLibrary.put('9',"----.");   codeLibrary.put('0',"-----");
        codeLibrary.put(' ',"/");
    }
    public static final java.util.Map<String, Character> letterLibrary;
    static{
        letterLibrary = new HashMap<>();
        letterLibrary.put(".-", 'a');   letterLibrary.put("-...", 'b');
        letterLibrary.put("-.-.", 'c'); letterLibrary.put("-..", 'd');
        letterLibrary.put(".", 'e');    letterLibrary.put("..-.", 'f');
        letterLibrary.put("--.",'g');   letterLibrary.put("....", 'h');
        letterLibrary.put("..", 'i');   letterLibrary.put(".---", 'j');
        letterLibrary.put("-.-", 'k');  letterLibrary.put(".-..", 'l');
        letterLibrary.put("--", 'm');   letterLibrary.put("-.", 'n');
        letterLibrary.put("---", 'o');  letterLibrary.put(".--.", 'p');
        letterLibrary.put("--.-", 'q'); letterLibrary.put(".-.", 'r');
        letterLibrary.put("...", 's');  letterLibrary.put("-", 't');
        letterLibrary.put("..-", 'u');  letterLibrary.put("...-", 'v');
        letterLibrary.put(".--", 'w');  letterLibrary.put("-..-", 'x');
        letterLibrary.put("-.--", 'y'); letterLibrary.put("--..", 'z');
        letterLibrary.put(".----", '1');        letterLibrary.put("..---", '2');
        letterLibrary.put("...--", '3');        letterLibrary.put(".----", '4');
        letterLibrary.put(".....", '5');        letterLibrary.put("-....", '6');
        letterLibrary.put("--...", '7');        letterLibrary.put("---..", '8');
        letterLibrary.put("----.", '9');        letterLibrary.put("-----", '0');
        letterLibrary.put("/", ' ');
    }


    public static String encode(final String userInput){
        final StringBuilder morse = new StringBuilder();

        for(int i = 0; i < userInput.length(); i++){
            if(i != 0){
                morse.append(' ');
            }
            if(codeLibrary.get(userInput.toLowerCase().charAt(i)) == null) { morse.append(userInput.charAt(i)); }
            else {  morse.append(codeLibrary.get(userInput.toLowerCase().charAt(i))); }

        }
        return morse.toString();
    }

    public static String decode(String userInput){
        String[] morseParts = userInput.split(" ");
        StringBuilder chars = new StringBuilder();
        for(String part : morseParts) {
            chars.append(letterLibrary.get(part));
        }
        return chars.toString();
    }

}