#include <stdio.h>
#include <stdlib.h>

typedef struct PCB pcb;
struct PCB
{
    int pid;
    double priority;
    int arrive_time;
    int burst_time;
    int remain_time;
    int start_time;
    pcb* next;
};
//구조체생성


pcb jobQueue[10];
pcb readyQueue[10];

////////////////////////////

void fcfs(void);
void priority();
void roundRobin();

void processSort(pcb* ptr);

////////////////////////////

pcb* cPtr;
int cBurst_time=0;
int cWaiting_time=0;
int tWaiting_time=0;

double alpha=0.3;
int quantum = 2;

pcb* pfirst;
pcb* lastPtr;


int main(void)
{
    int i=0;
    
    FILE* rfile = fopen("/Users/youngnamwoo/MyProject/Proj_CPUScheduling/Scheduling/process.txt", "rt");
    pfirst = NULL;
    if(rfile == NULL)
    {
        printf("File opne errer! \n");
    } // 파일오픈
    
    while(fscanf(rfile,"%d %lf %d %d",&jobQueue[i].pid,&jobQueue[i].priority,&jobQueue[i].arrive_time,&jobQueue[i].burst_time)!=EOF)
    {
        jobQueue[i].remain_time=jobQueue[i].burst_time;
        i++;
    }// 파일로부터 정보 가져와 jobQueue에 저장
    
    for(i=0; i<10; i++)
    {
        readyQueue[i].pid = jobQueue[i].pid;
        readyQueue[i].priority = jobQueue[i].priority;
        readyQueue[i].arrive_time = jobQueue[i].arrive_time;
        readyQueue[i].burst_time = jobQueue[i].burst_time;
        readyQueue[i].remain_time = jobQueue[i].remain_time;
    } // readyQueue에 jobQueue 값 저장 완료
    
    
    cPtr = readyQueue;
    fcfs();
    printf("\n\n\n");
    priority();
    for(int i=0;i<10;i++)
        readyQueue[i].remain_time=readyQueue[i].burst_time;
    printf("\n\n\n");
    roundRobin();
    
    fclose(rfile);
}


void roundRobin(){
    pcb* ptr=&readyQueue[0];
    int time=0;
    int finish=0;
    int i=0;
    int wait=0;
    int response=0;
    int turnaround=0;
    int idle=0;
    while(1){
        for(int i=0;i<10;i++){
            if(time==readyQueue[i].arrive_time){
                readyQueue[i].start_time=time;
                printf("<time %d> [new arrival] process %d \n",time,readyQueue[i].pid);
            }
        }
        
        
        if(finish==10){
            printf("cpu :%3.1lf \n",(double)((time-idle)/time)*100);
            printf("waiting : %3.1lf\n",(double)wait/10);
            printf("response : %3.1lf\n",(double)response/10);
            printf("turnaround : %3.1lf\n",(double)turnaround/10);
            printf("finish the roundRobin\n");
            break;
        }
        
        if(ptr!=NULL){
            int j=ptr->pid-1;
            int count=0;
            j=(j+1)%10;
            for(int i=j;i<10;i=(i+1)%10) {
                if(readyQueue[i].remain_time>0&&readyQueue[i].arrive_time<=time){
                    if(readyQueue[i].remain_time==readyQueue[i].burst_time){
                        readyQueue[i].start_time=time;
                    }
                    ptr= &readyQueue[i];
                    break;
                }
                if(count==10){
                    ptr= NULL;
                    break;
                }
                count++;
            }
        }
        
        for(int i=0;i<quantum;i++){
            if(ptr!=NULL){
                printf("<time %d> process %d running\n",time,ptr->pid);
                ptr->remain_time--;
                if(ptr->remain_time==0){
                    wait+=time-(ptr->arrive_time+ptr->burst_time);
                    response+=ptr->start_time-ptr->arrive_time;
                    turnaround+=time-ptr->arrive_time;
                    finish++;
                    printf("finish %d process\n",ptr->pid);
                    time++;
                    break;
                }
                
                time++;
            }
            i++;
        }
    }
}


void priority(){
    void arrival(int );
    pcb* maximumPriority(int );
    void upPriority(int ,int );
    int time=0;
    int finish=0;
    int wait=0;
    int response=0;
    int turnaround=0;
    int idle=0;
    pcb *queue=NULL;
    while(1){
        if(finish==10){
            printf("cpu :%3.1lf \n",(double)((time-idle)/time)*100);
            printf("waiting : %3.1lf\n",(double)wait/10);
            printf("response : %3.1lf\n",(double)response/10);
            printf("turnaround : %3.1lf\n",(double)turnaround/10);
            printf("Finish the Priority\n");
            break;
        }
        for(int i=0;i<10;i++){
            if(time==readyQueue[i].arrive_time){
                readyQueue[i].start_time=time;
                printf("<time %d> [new arrival] process %d\n",time,readyQueue[i].pid);
            }
        }
        
        int max;
        double maxPriority=0;
        maxPriority=-1;
        max=-1;
        for(int i=0;i<10;i++){
            if(readyQueue[i].arrive_time<=time&&readyQueue[i].remain_time>0&&maxPriority<=readyQueue[i].priority)
            {
                maxPriority=readyQueue[i].priority;
                max=i;
                if(readyQueue[i].remain_time==readyQueue[i].burst_time)
                    readyQueue[i].start_time=time;
            }
        }
        if(max==-1){
            queue=NULL;
        }
        else
            queue=&readyQueue[max];
        
        if(queue==NULL){
            printf("<time %d> cpu idle\n",time);
            idle++;
        }
        else{
            for(int i=0;i<10;i++){
                if(time>readyQueue[i].arrive_time&&readyQueue[i].remain_time>0&&queue->pid!=readyQueue[i].pid){
                    readyQueue[i].priority+=alpha;
                }
            }printf("<time %d> process %d running\n",time,queue->pid);
            queue->remain_time--;
            if(queue->remain_time==0){
                wait+=time-(queue->arrive_time+queue->burst_time);
                response+=queue->start_time-queue->arrive_time;
                turnaround+=time-queue->arrive_time;
                printf("finish %d process\n",queue->pid);
                finish++;
            }
        }
        time++;
    }
}



void fcfs(void)
{
    int i, j;
    int k=0;
    int idle_time=0;
    processSort(cPtr);
    //readyQueue arrive타임대로 정렬완료, arrive타임대로 정렬된 상태
    
    int time=0;
    for(i=0; i<10; i++)
    {
        for(j=0; j<readyQueue[i].burst_time; j++)
        {
            if(time== readyQueue[k].arrive_time)
            {
                printf("<time %d> [new arrival] process %d\n", time, readyQueue[k].pid);
                k++;
            }
            printf("<time %d> process %d running\n", time, readyQueue[i].pid);
            time++;
        }
        
    }
    for(i=0; i<10; i++)
    {
        cBurst_time = cBurst_time + readyQueue[i].burst_time;
        cWaiting_time = cBurst_time - readyQueue[i+1].arrive_time;
        tWaiting_time = tWaiting_time + cWaiting_time;
    }
    
    
    printf("==========================\n");
    printf("Average cpu usage : %3.1lf  \n" , (double)((tBurst_time()-idle_time)/tBurst_time())*100);
    printf("Average waiting time : %3.1lf\n", (double)tWaiting_time/10);
    printf("Average response time : %3.1lf\n", (double)tWaiting_time/10);
    printf("Average turnaround time : %3.1lf\n",(double)(tWaiting_time/10 + tBurst_time()/10));
    printf("Finish the FCFS\n");
    
}

int tBurst_time()
{
    int i=0;
    int tBurst=0;
    for(i=0; i<10; i++)
    {
        tBurst = tBurst+readyQueue[i].burst_time;
    }
    return tBurst;
}

void processSort(pcb* ptr)
{
    pcb temp;
    int i, j;
    for(i = 9; i>0; i--)
    {
        for(j=0; j<i; j++)
        {
            if(ptr[j].arrive_time > ptr[j+1].arrive_time)
            {
                temp = ptr[j+1];
                ptr[j+1] = ptr[j];
                ptr[j] = temp;
            }
            else if(ptr[j].arrive_time == ptr[j+1].arrive_time && ptr[j].pid > ptr[j+1].pid)
            {
                temp = ptr[j+1];
                ptr[j+1] = ptr[j];
                ptr[j] = temp;
            }
        }
    }
}