package com.STO.MHP.views;

import java.util.ResourceBundle;

public enum FxmlView {

    USER {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("user.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Client.fxml";
        }
    },
    PACKAGE {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("package.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Package.fxml";
        }
    },
    EVENT {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("event.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Event.fxml";
        }
    },
    LOGIN {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Login.fxml";
        }
    };

    public abstract String getTitle();

    public abstract String getFxmlFile();

    String getStringFromResourceBundle(String key) {
        return ResourceBundle.getBundle("Bundle").getString(key);
    }

}
