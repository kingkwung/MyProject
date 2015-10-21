#include<stdio.h>
#include<stdlib.h>
#include<string.h>

typedef struct smartPhone_info smartPhone_info;
typedef struct node_for_search node_for_search;
typedef struct forPointing forPointing;
struct smartPhone_info
{
    char model[50];             //모델명
    char vendor[20];            //판매회사
    double display;           //디스플레이
    char screen_resolution[20]; //스크린사이즈
    double screen_resolution_double; // double타입의 스크린사이즈
    char os[20];                //운영체제
    char cpu_type[20];          //cpu타입
    double cpu_clock;         //cpu클럭(헤르츠)
    double memory_size;       //메모리사이즈
    double price;             //가격
    
    smartPhone_info* next;
};

struct node_for_search
{
    char model[50];              //모델명
    char vendor[20];             //판매회사
    char display[10];            //디스플레이
    char screen_resolution[20];//스크린사이즈
    char os[20];                 //운영체제
    char cpu_type[20];           //cpu타입
    char cpu_clock[10];          //cpu클럭(헤르츠)
    char memory_size[10];        //메모리사이즈
    char price[15];              //가격
    
    double display_min;
    double display_max;
    char screen_min[20];
    char screen_max[20];
    double screen_double_min;
    double screen_double_max;
    double cpuclock_min;
    double cpuclock_max;
    double memory_min;
    double memory_max;
    double price_min;
    double price_max;
    
};
struct forPointing
{
    smartPhone_info* ptr;
};

void insertNode(smartPhone_info** head, smartPhone_info* key); //셋업, 추가 두 가지 경우에 사용하는 함수
void deleteNode(smartPhone_info** head, char* delete_model); // 노드지우는 함수
void updateToFile(smartPhone_info** head); // 업데이트된 정보를 파일에 쓰는 함수
void printAllNode(smartPhone_info** head); // 모든 정보를 출력하는 함수
void sorting(smartPhone_info**head, char* info_type, char* sort_type); // 정렬하는 함수
void swap(forPointing* a, forPointing* b); // 주소를 swap하는 함수
void search(smartPhone_info** head, node_for_search* search); // 검색을 위한 함수
int sliding_window(char* origin, char* search); // partial search를 위해 사용되는 함수
void change_screen_resolution_for_search(node_for_search* input_node); // 해상도 검색을 위해 문자열을 double 형으로 바꾸어 저장하는 기능을 하는 함수
void change_screen_resolution_for_insert(smartPhone_info* input_node); // insert시 해상도 설정을 위해 문자열해상도를 double해상도로 바꾸어 저장하는 기능을 하는 함수

void free_smartPhone_info(smartPhone_info** head);

int numOfInfo=0; // 총 정보의 갯수를 표시해주는 변수

int main(void)
{
    char deleteModelName[30]; // 삭제모델명을 저장할 변수
    char select[2]; // 메뉴선택에 사용되는 변수
    
    smartPhone_info* head = NULL;
    smartPhone_info tempForSend; // setup from file과 insertNode함수로 보낼 때 편하게 보내주기 위해 사용되는 노드
    node_for_search tempForSearch; // 검색함수에 조건을 보내기 위해 사용되는 노드
    
    //링크드리스트로 셋어하는 과정
    FILE* readFile = fopen("smartPhone.txt","r");
    while(fscanf(readFile, "%s %s %lf %s %s %s %lf %lf %lf",tempForSend.model,tempForSend.vendor,&tempForSend.display,tempForSend.screen_resolution,tempForSend.os,tempForSend.cpu_type,&tempForSend.cpu_clock,&tempForSend.memory_size,&tempForSend.price)!=EOF)
    {
        change_screen_resolution_for_insert(&tempForSend);
        insertNode(&head, &tempForSend); // 링크드리스트로 정보 넣는 함수
    }
    /*printAllNode(&head);*/
    fclose(readFile);
    printf("[[SET UP FROM FILE COMPLETE]]\n");
    //
    
    
    while(1){
        printf("\n====Current Information : [%d]====\n",numOfInfo);
        printf("| 1. insert\t\t\t\t |\n| 2. delete\t\t\t\t |\n| 3. partial search\t\t\t |\n| 4. sorting\t\t\t\t |\n| 5. show All Info\t\t\t |\n| 6. exit\t\t\t\t\t |\n");
        printf("===================================\n--> ");
        
        scanf("%s",select);
        
        //추가하는 과정
        if(strcmp(select,"1")==0)
        {
            printf("\n-INSERT START-\n");
            printf("Enter the insert information : ");
            scanf("%s %s %lf %s %s %s %lf %lf %lf",tempForSend.model,tempForSend.vendor,&tempForSend.display,tempForSend.screen_resolution,tempForSend.os,tempForSend.cpu_type,&tempForSend.cpu_clock,&tempForSend.memory_size,&tempForSend.price);
            
            change_screen_resolution_for_insert(&tempForSend);
            insertNode(&head, &tempForSend);
            
            updateToFile(&head); //갱신된 내용을 파일에 다시 써주는 과정
        }
        
        
        //지우는 과정
        else if(strcmp(select,"2")==0)
        {
            printf("\n-DELETE START-\n");
            printf("Enter the delete model : ");
            scanf("%s",deleteModelName);
            deleteNode(&head, deleteModelName);
            /*printAllNode(&head);*/
            
            updateToFile(&head); //갱신된 내용을 파일에 다시 써주는 과정
        }
        
        //검색하는 과정
        else if(strcmp(select,"3")==0)
        {
            printf("\n-PARTIAL_SEARCH START-\n\n");
            printf("***************************  [NOICE!]  ***************************\n* (If you want to ignore specific condition, Enter the '0')      *\n* (For Numeric case, If you want to use it, Enter the '1' first.)*\n******************************************************************\n\n");
            while(1){
            printf("Enter the search model : ");
            scanf("%s",tempForSearch.model);
            
            printf("Enter the search vendor : ");
            scanf("%s",tempForSearch.vendor);
            
            printf("Enter the search display : ");
            scanf("%s",tempForSearch.display);
                if(strcmp(tempForSearch.display,"1")!=0 && strcmp(tempForSearch.display,"0")!=0)
                {printf("[ERROR] You Enter the WRONG VALUE, TRY AGAIN!\n\n"); continue;}
            if(strcmp(tempForSearch.display,"1")==0)
            {
                printf("  -> minimum of display? : ");
                scanf("%lf",&tempForSearch.display_min);
                printf("  -> maximum of display? : ");
                scanf("%lf",&tempForSearch.display_max);
            }
            
            
            printf("Enter the search screen resolution : ");
            scanf("%s",tempForSearch.screen_resolution);
                if(strcmp(tempForSearch.screen_resolution,"1")!=0 && strcmp(tempForSearch.screen_resolution,"0")!=0)
                {printf("[ERROR] You Enter the WRONG VALUE, TRY AGAIN!\n\n"); continue;}
            if(strcmp(tempForSearch.screen_resolution,"1")==0)
            {
                printf("  -> minimum of screen resolution?[ex) 1080*1920] : ");
                scanf("%s",tempForSearch.screen_min);
                printf("  -> maximum of screen resolution?[ex) 1080*1920] : ");
                scanf("%s",tempForSearch.screen_max);
                change_screen_resolution_for_search(&tempForSearch); // 해상도의 경우 문자열이기 때문에 이 것을 double형의 값으로 바꾸어 다시 저장해줘야한다
            }
            
            printf("Enter the search operating system : ");
            scanf("%s",tempForSearch.os);
            
            printf("Enter the search cpu type : ");
            scanf("%s",tempForSearch.cpu_type);
            
            printf("Enter the search cpu clock : ");
            scanf("%s",tempForSearch.cpu_clock);
                if(strcmp(tempForSearch.cpu_clock,"1")!=0 && strcmp(tempForSearch.cpu_clock,"0")!=0)
                {printf("[ERROR] You Enter the WRONG VALUE, TRY AGAIN!\n\n"); continue;}
            if(strcmp(tempForSearch.cpu_clock,"1")==0)
            {
                printf("  -> minimum of cpu clock? : ");
                scanf("%lf",&tempForSearch.cpuclock_min);
                printf("  -> maximum of cpu clock? : ");
                scanf("%lf",&tempForSearch.cpuclock_max);
            }
            
            printf("Enter the search memory size : ");
            scanf("%s",tempForSearch.memory_size);
                if(strcmp(tempForSearch.memory_size,"1")!=0 && strcmp(tempForSearch.memory_size,"0")!=0)
                {printf("[ERROR] You Enter the WRONG VALUE, TRY AGAIN!\n\n"); continue;}
            if(strcmp(tempForSearch.memory_size,"1")==0)
            {
                printf("  -> minimum of memory size? : ");
                scanf("%lf",&tempForSearch.memory_min);
                printf("  -> maximum of memory size? : ");
                scanf("%lf",&tempForSearch.memory_max);
            }
            
            printf("Enter the search price : ");
            scanf("%s",tempForSearch.price);
                if(strcmp(tempForSearch.price,"1")!=0 && strcmp(tempForSearch.price,"0")!=0)
                {printf("[ERROR] You Enter the WRONG VALUE, TRY AGAIN!\n\n"); continue;}
            if(strcmp(tempForSearch.price,"1")==0)
            {
                printf("  -> minimum of price? : ");
                scanf("%lf",&tempForSearch.price_min);
                printf("  -> maximum of price? : ");
                scanf("%lf",&tempForSearch.price_max);
            }
                break;
            }
            search(&head, &tempForSearch);
        }
        
        //정렬하는 과정
        else if(strcmp(select,"4")==0)
        {
            char info_type[20];
            char sort_type[20];
            printf("\n-SORTING START-\n\n");
            printf("Enter the information type\n(ex)model / vendor / display size / screen resolution / operating system / cpu type / cpu clock / memory size / price)\n : ");
            getchar();
            scanf("%[^\n]",info_type);
            printf("\n");
            printf("Enter the sort type\n(ex)ascending order / descending order)\n : ");
            getchar();
            scanf("%[^\n]",sort_type);
            
            sorting(&head, info_type, sort_type);
        }
        
        //모든정보 출력
        else if(strcmp(select,"5")==0)
        {
            printf("\n-SHOW ALL INFORMATION-\n");
            printAllNode(&head);
            printf("\n");
        }
        //프로그램 종료
        else if(strcmp(select,"6")==0)
        {
            printf("\n--[PROGRAM TERMINATED]--\n");
            printf("--[All Node FREED]--\n");
            free_smartPhone_info(&head);
            exit(0);
        }
        else
        {
            printf("[enter the correct menu..]\n\n");
        }
        
    }
}
void free_smartPhone_info(smartPhone_info** head)
{
    smartPhone_info* current = *head;
    smartPhone_info* prev = NULL;
    while(current){
        prev = current;
        current = current->next;
        free(prev);
    }
}

void search(smartPhone_info** head, node_for_search* search)
{
    smartPhone_info* current = *head;
    
    printf("\n");
    while(current)
    {
        if((strcmp(search->model,"0")==0 || sliding_window(current->model, search->model)==1) && (strcmp(search->vendor,"0")==0 || sliding_window(current->vendor, search->vendor)==1) && (strcmp(search->os,"0")==0 ||sliding_window(current->os, search->os)==1) && (strcmp(search->cpu_type,"0")==0 || sliding_window(current->cpu_type, search->cpu_type)==1))
        {
            if((strcmp(search->display,"1")!=0 || (search->display_min<=current->display && current->display<=search->display_max)) && (strcmp(search->cpu_clock,"1")!=0 || (search->cpuclock_min<=current->cpu_clock && current->cpu_clock<=search->cpuclock_max)) && (strcmp(search->memory_size,"1")!=0 || (search->memory_min<=current->memory_size && current->memory_size<=search->memory_max)) && (strcmp(search->price,"1")!=0 || (search->price_min<=current->price && current->price<=search->price_max)) && (strcmp(search->screen_resolution,"1")!=0 || (search->screen_double_min<=current->screen_resolution_double && current->screen_resolution_double<=search->screen_double_max)))
            {
                printf("%-30s / %-10s / %-5.2lf / %-10s / %-10s / %-15s / %-5.2lf / %-5.2lf / %-10.2lf\n",current->model,current->vendor,current->display,current->screen_resolution, current->os, current->cpu_type,current->cpu_clock,current->memory_size, current->price);
            }
        }
        current = current->next;
    }
}

void change_screen_resolution_for_insert(smartPhone_info* input_node)
{
    char screen[20];
    strcpy(screen,input_node->screen_resolution);
    
    char* tok;
    double first;
    double second;
    
    tok = strtok(screen,"*");
    first = atof(tok);
    tok = strtok(NULL," ");
    second = atof(tok);
    input_node->screen_resolution_double= first*second;
    
    //printf("%.2lf\n",input_node->screen_resolution_double);
}

void change_screen_resolution_for_search(node_for_search* input_node)
{
    char screen_min[20];
    char screen_max[20];
    strcpy(screen_min,input_node->screen_min);
    strcpy(screen_max,input_node->screen_max);
    
    char* tok;
    double first;
    double second;
    
    tok = strtok(screen_min,"*");
    first = atof(tok);
    tok = strtok(NULL," ");
    second = atof(tok);
    input_node->screen_double_min = first*second;
    
    
    tok = strtok(screen_max,"*");
    first = atof(tok);
    tok = strtok(NULL, " ");
    second = atof(tok);
    input_node->screen_double_max = first*second;
    
    //printf("%lf / %lf\n",input_node->screen_double_min,input_node->screen_double_max);
}

int sliding_window(char* origin, char* search)
{
    int i, j;
    int matchChar=0;
    
    for(i=0; i<strlen(origin)-(strlen(search)-1); i++){
        for(j=0; j<strlen(search); j++){
            if(search[j]==origin[i+j]){
                matchChar++;
            }
        }
        if(matchChar==strlen(search)){
            return 1;
        }
        matchChar=0;
    }
    return 0;
}

void swap(forPointing* a, forPointing* b)
{
    forPointing temp;
    temp = *a;
    *a = *b;
    *b = temp;
}

void sorting(smartPhone_info**head, char* info_type, char* sort_type)// 정렬하는 함수
{
    int i, j;
    int k=0;
    int printCheck=1;
    smartPhone_info* current = *head;
    forPointing* usedForPointing = (forPointing*)malloc(sizeof(forPointing)*numOfInfo);
    
    while(current)
    {
        usedForPointing[k].ptr = current;
        current = current->next;
        k++;
    }k=0;
    
    printf("\n[NOTICE!] UPPER CASE < LOWER CASE\n\n");
    
    //모델명에 관한 정렬
    if(strcmp(info_type,"model")==0)
    {
        if(strcmp(sort_type,"ascending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(strcmp(usedForPointing[j].ptr->model,usedForPointing[j+1].ptr->model)>0){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else if(strcmp(sort_type,"descending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(strcmp(usedForPointing[j].ptr->model,usedForPointing[j+1].ptr->model)<0){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else
        {
            printf("[ERROR] sorting type error!\n\n");
            printCheck=0;
        }
    }
    
    //판매회사에 관한 정렬
    else if(strcmp(info_type,"vendor")==0)
    {
        if(strcmp(sort_type,"ascending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(strcmp(usedForPointing[j].ptr->vendor,usedForPointing[j+1].ptr->vendor)>0){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else if(strcmp(sort_type,"descending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(strcmp(usedForPointing[j].ptr->vendor,usedForPointing[j+1].ptr->vendor)<0){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else
        {
            printf("[ERROR] sorting type error!\n\n");
            printCheck=0;
        }
    }
    
    //디스플레이에 관한 정렬
    else if(strcmp(info_type,"display size")==0)
    {
        if(strcmp(sort_type,"ascending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(usedForPointing[j].ptr->display>usedForPointing[j+1].ptr->display){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else if(strcmp(sort_type,"descending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(usedForPointing[j].ptr->display<usedForPointing[j+1].ptr->display){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else
        {
            printf("[ERROR] sorting type error!\n\n");
            printCheck=0;
        }
    }
    
    //스크린사이즈에 관한 정렬
    else if(strcmp(info_type,"screen resolution")==0)
    {
        if(strcmp(sort_type,"ascending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(usedForPointing[j].ptr->screen_resolution_double>usedForPointing[j+1].ptr->screen_resolution_double){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else if(strcmp(sort_type,"descending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(usedForPointing[j].ptr->screen_resolution_double<usedForPointing[j+1].ptr->screen_resolution_double){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else
        {
            printf("[ERROR] sorting type error!\n\n");
            printCheck=0;
        }
    }
    
    //운영체제에 관한 정렬
    else if(strcmp(info_type,"operating system")==0)
    {
        if(strcmp(sort_type,"ascending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(strcmp(usedForPointing[j].ptr->os,usedForPointing[j+1].ptr->os)>0){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else if(strcmp(sort_type,"descending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(strcmp(usedForPointing[j].ptr->os,usedForPointing[j+1].ptr->os)<0){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else
        {
            printf("[ERROR] sorting type error!\n\n");
            printCheck=0;
        }
    }
    
    //cpu 타입
    else if(strcmp(info_type,"cpu type")==0)
    {
        if(strcmp(sort_type,"ascending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(strcmp(usedForPointing[j].ptr->cpu_type,usedForPointing[j+1].ptr->cpu_type)>0){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else if(strcmp(sort_type,"descending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(strcmp(usedForPointing[j].ptr->cpu_type,usedForPointing[j+1].ptr->cpu_type)<0){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else
        {
            printf("[ERROR] sorting type error!\n\n");
            printCheck=0;
        }
    }
    
    //cpu 클럭
    else if(strcmp(info_type,"cpu clock")==0)
    {
        if(strcmp(sort_type,"ascending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(usedForPointing[j].ptr->cpu_clock>usedForPointing[j+1].ptr->cpu_clock){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else if(strcmp(sort_type,"descending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(usedForPointing[j].ptr->cpu_clock<usedForPointing[j+1].ptr->cpu_clock){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else
        {
            printf("[ERROR] sorting type error!\n\n");
            printCheck=0;
        }
    }
    
    //메모리 사이즈
    else if(strcmp(info_type,"memory size")==0)
    {
        if(strcmp(sort_type,"ascending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(usedForPointing[j].ptr->memory_size>usedForPointing[j+1].ptr->memory_size){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else if(strcmp(sort_type,"descending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(usedForPointing[j].ptr->memory_size<usedForPointing[j+1].ptr->memory_size){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else
        {
            printf("[ERROR] sorting type error!\n\n");
            printCheck=0;
        }
    }
    
    //가격
    else if(strcmp(info_type,"price")==0)
    {
        if(strcmp(sort_type,"ascending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(usedForPointing[j].ptr->price>usedForPointing[j+1].ptr->price){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else if(strcmp(sort_type,"descending order")==0)
        {
            for(i=0; i<numOfInfo-1; i++)
                for(j=0; j<numOfInfo-1-i; j++)
                    if(usedForPointing[j].ptr->price<usedForPointing[j+1].ptr->price){
                        swap(&usedForPointing[j],&usedForPointing[j+1]);}
        }
        else
        {
            printf("[ERROR] sorting type error!\n\n");
            printCheck=0;
        }
    }
    
    //예외처리
    else
    {
        printf("[ERROR] information type error!\n\n");
        printCheck=0;
    }
    
    
    if(printCheck==1){
        //정렬이 완료된후 정렬완료된 정보를 출력해준다
        for(i=0; i<numOfInfo; i++)
            printf("%-30s / %-10s / %-5.2lf / %-10s / %-10s / %-15s / %-5.2lf / %-5.2lf / %-10.2lf\n",usedForPointing[i].ptr->model,usedForPointing[i].ptr->vendor,usedForPointing[i].ptr->display,usedForPointing[i].ptr->screen_resolution, usedForPointing[i].ptr->os, usedForPointing[i].ptr->cpu_type,usedForPointing[i].ptr->cpu_clock,usedForPointing[i].ptr->memory_size, usedForPointing[i].ptr->price);
        
        printf("\n[SORTED INFORMATION!]\n");
    }
}

void updateToFile(smartPhone_info** head)
{
    smartPhone_info* current = *head;
    
    FILE* writeFILE = fopen("smartPhone.txt","w");
    
    while(current){
        fprintf(writeFILE, "%s %s %lf %s %s %s %lf %lf %lf\n",current->model,current->vendor,current->display,current->screen_resolution,current->os,current->cpu_type,current->cpu_clock,current->memory_size,current->price);
        current = current->next;
    }
    
    
    fclose(writeFILE);
    
    printf("[FILE UPDATE SUCCESS!]\n");
}
void printAllNode(smartPhone_info** head)
{
    smartPhone_info* current = *head;
    
    while(current){
        printf("%-30s / %-10s / %-5lf / %-10s / %-10s / %-15s / %-5lf / %-5lf / %-10lf\n",current->model,current->vendor,current->display,current->screen_resolution,current->os,current->cpu_type,current->cpu_clock,current->memory_size,current->price);
        
        current = current->next;
    }
}

void insertNode(smartPhone_info** head, smartPhone_info* key)
{
    smartPhone_info* current = *head;
    smartPhone_info* prev = NULL;
    
    smartPhone_info* newNode = (smartPhone_info*)malloc(sizeof(smartPhone_info)*1);
    *newNode = *key;
    
    while(current)
    {
        if(strcmp(current->model,newNode->model)==0) // 중복된 model name 사용여부 검사
        {
            printf("[ERROR] You can't insert the duplicated model\n\n");
            return;
        }
        
        prev = current;
        current = current->next;
    }
    
    if(prev){
        prev->next = newNode;
        newNode->next = current;
    }
    else{
        newNode->next = current;
        *head = newNode;
    }
    numOfInfo++;
}

void deleteNode(smartPhone_info** head, char* delete_model)
{
    smartPhone_info* current = *head;
    smartPhone_info* prev = NULL;
    
    while(current)
    {
        if(strcmp(current->model,delete_model)==0) // 만약 해당노드에 지우고자 하는 이름이 있으면 break;
        {
            break;
        }
        prev = current;
        current = current->next;
    }
    
    if(current)
    {
        if(prev){
            prev->next = current->next;
        }
        else{
            *head = current->next;
        }
        free(current);
        printf("[DELETE SUCCESS!]\n\n");
        numOfInfo--;
    }
    else
    {
        printf("[ERROR]delete name does not exist!\n\n");
    }
}
