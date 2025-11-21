# 🟢 BPMN Runner API

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-v3.2-green)
![License](https://img.shields.io/badge/License-MIT-blue)

## 🌐 Demo

* Acesse a Documentação: [Doc do Projeto](https://bpmn-runner.dev/)
* URL da API: [Api](https://api.bpmn-runner.app)

## 📖 Descrição

Este projeto é uma aplicação Spring Boot que tem como objetivo **\[descrever o objetivo do projeto]**.
Ele inclui **\[listar funcionalidades principais]**, e foi desenvolvido com foco em **\[ex.: backend robusto, API RESTful]**.

---

## 🛠 Tecnologias Utilizadas

* **Spring Boot v3.2**
* **Java 17+**
* **Maven / Gradle**

---

## 💻 Pré-requisitos

Antes de iniciar, você precisa ter instalado:

* Java 17 ou superior
* Maven ou Gradle
* Firebase (Firestore project)

Verifique a versão do Java:

```bash
java -version
```

Verifique a versão do Maven:

```bash
mvn -v
```

---


## ⚡ Instalação

1. Clone o repositório:

```bash
git clone https://gitlab.com/aluno-ufn/bpmn-runner-api.git
```

2. Acesse a pasta do projeto:

```bash
cd nome-do-projeto
```

3. Configure o arquivo `application.properties` ou `application.yml` com as informações do seu banco de dados e outras variáveis necessárias.

4. Instale as dependências e construa o projeto:

```bash
mvn clean install
```

ou

```bash
gradle build
```

---

## Como baixar o arquivo JSON de configuração do Firebase
1. Acesse o Firebase Console

👉 https://console.firebase.google.com/

Escolha o seu projeto.

2. Abra “Configurações do projeto”

No menu esquerdo:

⚙️ Ícone de engrenagem → Configurações do projeto

3. Vá até a aba “Contas de serviço”

No topo, clique em:

Contas de serviço

4. Clique em “Gerar nova chave privada”

Aparece um card com o Google Cloud Service Account.

Lá tem um botão:

➡️ Gerar nova chave privada

O Firebase vai:

gerar uma chave nova

baixar um arquivo .json no seu computador

renomeie o arquivo para "bpmn-runner-account.json"

cole o arquivo na pasta: `bpmn-runner-api/src/main/resources/bpmn-runner-account.json`

---

## 🚀 Execução

Para rodar a aplicação em ambiente de desenvolvimento:

```bash
mvn spring-boot:run
```

ou

```bash
gradle bootRun
```

A aplicação estará disponível em `http://localhost:8080`.

---

## 🗂 Estrutura do Projeto

```
bpmn-runner-api/
│
├─ src/main/java/com/seuusuario/projeto/
│  ├─ controller/      # Controladores REST
│  ├─ service/         # Serviços de negócio
│  ├─ repository/      # Repositórios JPA
│  ├─ model/           # Entidades
│  ├─ dto/             # Data Transfer Objects (opcional)
│  └─ BpmnRunnerApiApplication.java
│
├─ src/main/resources/
│  └─ application.properties  # Configurações do Spring Boot
│
├─ pom.xml ou build.gradle
└─ README.md
```

---

## 📄 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.
