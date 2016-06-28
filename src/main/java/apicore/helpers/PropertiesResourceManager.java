package apicore.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class-wrapper for working with properties-файлами
 * Для доступа к файлам используется относительный путь (имя ресурса)
 */
public final class PropertiesResourceManager {

	private Properties properties = new Properties();

	public PropertiesResourceManager() {
		properties = new Properties();
	}

	public PropertiesResourceManager(final String resourceName) {
		properties = appendFromResource(properties, resourceName);
	}

	/**
	 * Объединение двух properties-файлов (параметры из 2-го файла переопределяют параметры из 1-го)
	 * @param objProperties Properties
	 * @param resourceName Resource Name
	 * @return Properties
	 */
	private Properties appendFromResource(final Properties objProperties, final String resourceName) {
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);

		if (inStream != null) {
			try {
				objProperties.load(inStream);
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println(String.format("Resource \"%1$s\" could not be found", resourceName));
		}
		return objProperties;
	}

	/**
	 * Получение значения параметра по ключу
	 * @param key Key
	 * @return Value
	 */
	public String getProperty(final String key) {
		return properties.getProperty(key);
	}

	/**
	 * Получение значения параметра по ключу
	 * @param key Key
	 * @param defaultValue Default Value
	 * @return Value
	 */
	public String getProperty(final String key, final String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	/**
	 * Sets the property
	 * @param key Key
	 * @param value Value
	 */
	public void setProperty(final String key, final String value) {
		properties.setProperty(key, value);
	}
}
