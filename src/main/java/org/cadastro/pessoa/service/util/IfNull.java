package org.cadastro.pessoa.service.util;

 // Utilitário responsável por retornar outro valor quando o objeto for nulo.

public class IfNull {

    private IfNull() {
        // Garante que a classe não seja instanciada.
    }

    /**
     * Quando o parâmetro "value" for igual a null a função deve retornar o
     * valor do parâmetro "otherValue". <br>
     * Obs: O valor do parâmetro "otherValue" sempre deve ser do mesmo tipo do
     * parâmetro "value". <br>
     * Ex 1: <br>
     * String valor = null; <br>
     * String retorno = IfNull.get(valor,""); <br>
     * Ex 2: <br>
     * Long valor = null; <br>
     * Long retorno = IfNull.get(valor,0L);
     * 
     * @param value
     * @param otherValue
     * @return
     */
    public static <T> T get(T value, T otherValue) {
        if (value == null) {
            return otherValue;
        }
        return value;
    }

    public static <T> T conditionNE(T value, T condition, T otherValue) {

        if (value != condition) {
            return value;
        }

        return otherValue;
    }

    public static <T> T conditionEQ(Boolean value, T thenValue, T otherValue) {

        if (value) {
            return thenValue;
        }

        return otherValue;
    }

}