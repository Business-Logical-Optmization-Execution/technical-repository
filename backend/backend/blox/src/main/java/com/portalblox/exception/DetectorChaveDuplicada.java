package com.portalblox.exception;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Reconhece violações de unicidade do MySQL a partir de qualquer exceção de persistência,
 * percorrendo a cadeia de causas sem depender de classes internas do driver.
 */
public final class DetectorChaveDuplicada {

	private static final int MYSQL_DUPLICATE_KEY = 1062;

	private DetectorChaveDuplicada() {
	}

	public static boolean ehChaveDuplicada(Throwable erro) {
		Throwable atual = erro;
		while (atual != null) {
			if (atual instanceof SQLIntegrityConstraintViolationException sql
					&& sql.getErrorCode() == MYSQL_DUPLICATE_KEY) {
				return true;
			}
			if (atual instanceof SQLException sql && sql.getErrorCode() == MYSQL_DUPLICATE_KEY) {
				return true;
			}
			Throwable causa = atual.getCause();
			atual = causa == atual ? null : causa;
		}
		return false;
	}

}
