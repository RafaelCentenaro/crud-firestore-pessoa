# crud-firestore-pessoa

Este é um projeto construído com o framework Quarkus e será exemplificado um CRUD de pessoa utilizando o banco de dados Firestore.

## Quarkus

[Link para criar o projeto em quarkus.](https://code.quarkus.io/ "Quarkus") 

## Biblioteca


[Link da documentação da biblioteca utilizada.](https://docs.quarkiverse.io/quarkus-google-cloud-services/main/firestore.html "Quarkiverse") 



## Configuração

application.properties

    firestore.projectId=crud-firestore-pessoa
    firestore.collectionRoot=CollectionRoot
    firestore.collectionLeaf=dados
    firestore.credentialsFile=sua-chave.json
    
 - firestore.projectId - Nome do projeto utilizado no console do Firestore.
 - firestore.collectionRoot - Nome da collection principal dentro do banco de dados. Este nó poderia ser definido por cliente.
 - firestore.collectionLeaf - Nome da collection que representa o respositório dos registros dentro da "tabela".
 - firestore.credentialsFile - Arquivo de credencial obtida no console do Firestore. **OBs: O arquivo deverá ser armazenado em src/main/resources**
 
 
## Anotações

- @Collection - Define que a classe anotada será uma collection dentro do Firestore. Nesta anotação será necessário informa qual o nome da collection.
- @OrderBy - Define qual atributo será utilizado na ordernação.


## Classes

### FirestoreCollectionImpl

Classe responsável pela conexão ao Firestore e manipulação das collections.

### FirestoreRepositoyImpl

Classe ancestral padrão para implementação do repository. Esta classe possui a implementação de todos os métodos padrões para
manipulação da collection.


## Criando uma base no Firestore.


### 1º Acesse o console do Firestore
![](img/1.png)

Clique no botão "criar um projeto".

### 2º Informe um nome para o projeto.
![](img/2.png)

### 3º Configure o Google Analytics.
![](img/3.png)

### 4º Informe a localização para o Google Analytics.
![](img/4.png)

### 5º Aguarde.
![](img/5.png)

### 6º Clique no menu "Firestore Database".
![](img/6.png)

### 7º Clique em "Criar banco de dados".
![](img/7.png)

### 8º Inicie em modo produção. Clique em "Avançar"
![](img/8.png)

### 9º Defina o local do Cloud Firestore
![](img/9.png)

### 10º Aguarde
![](img/10.png)

### 11º Pronto! Base criada.
![](img/11.png)

### 12º Agora vamos gerar arquivo de credencial. Clique em "Configurações do projeto"
![](img/12.png))
### 13º Selecione java e clique em "Gerar nova chave privada"
![](img/13.png)

### 14º Clique em "Gerar chave"
![](img/14.png)

### 15º Cole a chave gerada dentro de src/main/resources. Lembre de editar o nome do arquivo dentro de applicatoin.properties
![](img/15.png)

